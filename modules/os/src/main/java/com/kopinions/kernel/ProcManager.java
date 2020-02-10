package com.kopinions.kernel;

import com.kopinions.Address;
import com.kopinions.core.CPU;
import com.kopinions.core.Registry.Name;
import com.kopinions.kernel.Proc.State;
import com.kopinions.mm.PMM;
import com.kopinions.mm.Page;
import com.kopinions.mm.PageBasePMM;
import com.kopinions.mm.PageBasedVMM;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.IntStream;

public class ProcManager {

  private Queue<Proc> running;
  private Queue<Proc> blocked;
  static Proc current;
  static Proc idle;
  private Selector<Proc> selector;
  private static Generator<Integer> ids = new IdGenerator();
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
    idle = new Proc(0);
    idle.need_resched = true;
    idle.awakened();
    current = idle;
  }

  Proc create(Job job) {
    Proc proc = new Proc(ids.generate());
    proc.state = State.CREATED;
    proc.priority = job.priority;
    running.add(proc);
    Page alloc = pmm.alloc();
    PageBasedVMM vmm = new PageBasedVMM(pmm, sm, alloc.pa());
    proc.vmm = vmm;
    Page code = vmm.pgdir().alloc(new Address(0));
    List<Short> instructions = job.instructions();
    int instructionSize = instructions.size();
    ByteBuffer jobData = ByteBuffer.allocate(instructionSize * 2);
    IntStream.range(0, instructionSize).forEach(i -> jobData.putShort(i*2, instructions.get(i)));
    code.setData(jobData.array());
    active(proc);
    return proc;
  }

  void kill(Proc proc) {

  }

  void active(Proc proc) {
    if (proc != current) {
      current = proc;

      // TODO change esp and preserve the context and switch to new process;
      cpu.registry().set(Name.EIP, current.context.eip);
      cpu.registry().set(Name.CR3, current.vmm.pgdir().as());
      proc.awakened();
    }
  }

  Proc id(int id) {
    return null;

  }

  public synchronized Proc current() {
    return current;
  }

  void report(Reporter<String> reporter) {
    reporter.report("");
  }

  private Proc select(Selector<Proc> selector) {
    return selector.applied(running).stream().findFirst().orElse(null);
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
    }
    else {
      current.need_resched = true;
    }
  }
}
