package com.kopinions.kernel;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.kopinions.Address;
import com.kopinions.core.CPU;
import com.kopinions.core.Registry.Name;
import com.kopinions.kernel.Proc.Context;
import com.kopinions.kernel.Proc.State;
import com.kopinions.mm.PMM;
import com.kopinions.mm.Page;
import com.kopinions.mm.PageBasedVMM;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

public class ProcManager implements Report<Map<String, Object>>{

  private Queue<Proc> running;
  private Queue<Proc> blocked;
  static Proc current;
  static Proc idle;
  private Selector<Proc> selector;
  private static Generator<Short> ids = new IdGenerator();
  private CPU cpu;
  private PMM pmm;
  private SwapManager sm;

  public ProcManager(Selector<Proc> procSelector, CPU cpu, PMM pmm, SwapManager sm) {
    selector = procSelector;
    this.cpu = cpu;
    this.pmm = pmm;
    this.sm = sm;
    this.running = new PriorityQueue<>();
    this.blocked = new PriorityQueue<>();
    idle = new Proc((short) 0);
    idle.need_resched = true;
    idle.awakened();
    current = idle;
  }

  public Proc create(Job job) {
    Proc proc = new Proc(ids.generate());
    proc.state = State.RUNNING;
    proc.priority = job.priority;
    proc.context = new Context();
    running.add(proc);
    Page alloc = pmm.alloc();
    PageBasedVMM vmm = new PageBasedVMM(pmm, sm, alloc.pa());
    proc.vmm = vmm;
    Page code = vmm.pgdir().alloc(new Address(0));
    proc.context.eip = 0;
    proc.context.ebp = (short) code.pa();
    proc.context.edi = proc.pid();
    List<Short> instructions = job.instructions();
    int instructionSize = instructions.size();
    ByteBuffer jobData = ByteBuffer.allocate(instructionSize * 2);
    range(0, instructionSize).forEach(i -> jobData.putShort(i * 2, instructions.get(i)));
    code.setData(jobData.array());
    active(proc);
    return proc;
  }

  void kill(Proc proc) {
    running.remove(proc);
    proc.killed();
    current = idle;
  }

  public void exit(Proc proc) {
    running.remove(proc);
    proc.exited();
    current = idle;
  }

  public void active(Proc proc) {
    if (proc != current) {
      current = proc;
      cpu.registry().backup();
      cpu.registry().set(Name.EIP, current.context.eip);
      cpu.registry().set(Name.EBP, current.context.ebp);
      cpu.registry().set(Name.CR3, current.vmm.pgdir().as());
      cpu.registry().set(Name.EDI, proc.pid());
      proc.awakened();
    }
  }

  public Proc id(short id) {
    if (id == 0) {
      return idle;
    }
    return running.stream().filter(r -> r.pid() == id).findFirst().orElse(null);
  }

  public synchronized Proc current() {
    return current;
  }

  public Proc select(Selector<Proc> selector) {
    Optional<Proc> proc = selector.applied(running).stream().map(i -> Optional.ofNullable(i))
        .findFirst().orElse(Optional.ofNullable(null));
    return proc.orElse(null);
  }

  public synchronized void schedule() {
    Proc next;
    current.need_resched = false;
    if (current.state == Proc.State.RUNNING) {
      if (current != idle) {
        running.add(current);
      }
    }
    if ((next = select(selector)) != null) {
      running.remove(next);
    }
    if (next == null) {
      next = idle;
    }
    next.runs++;
    if (next != current) {
      active(next);
    }
  }

  public void tick() {
    if (current != idle) {
      current.tick();
    } else {
      current.need_resched = true;
    }
  }

  @Override
  public void report(Reporter<Map<String, Object>> reporter) {
    Map<String, Object> status = new HashMap<>();
    Map<Object, Object> running = new HashMap<>();
    running.put("count", running.size());
    running.put("ids", this.running.stream().map(Proc::pid).collect(toList()));
    status.put("running", running);

    reporter.report(status);
  }
}
