package com.kopinions.kernel;

import com.kopinions.kernel.Proc.State;
import com.kopinions.mm.PMM;
import com.kopinions.mm.Page;
import com.kopinions.mm.PageBasePMM;
import com.kopinions.mm.PageBasedVMM;
import java.util.PriorityQueue;
import java.util.Queue;

public class ProcManager {

  private Queue<Proc> ready;
  private Queue<Proc> created;
  private Queue<Proc> running;
  private Queue<Proc> blocked;
  static Proc current;
  static Proc idle;
  private Selector<Proc> selector;
  private static Generator<Integer> ids = new IdGenerator();

  public ProcManager(Selector<Proc> procSelector) {
    selector = procSelector;
    this.created = new PriorityQueue<>();
    this.ready = new PriorityQueue<>();
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
    created.add(proc);
    PMM instance = PageBasePMM.instance(Kernel.MEM_USERSPACE_SIZE, Kernel.PAGE_SIZE);
    Page alloc = instance.alloc();
    PageBasedVMM pageBasedVMM = new PageBasedVMM(alloc.pa());
    proc.vmm = pageBasedVMM;

    // create pdt for the current process
    // create vmm for current process
    // create swap manager for current process
    return proc;
  }

  void kill(Proc proc) {

  }

  void active(Proc proc) {
    if (proc != current) {
      current = proc;

      // TODO change esp and preserve the context and switch to new process;
      // load_esp0(next->kstack + KSTACKSIZE);
      // lcr3(next->cr3);
      // switch_to(&(prev->context), &(next->context));
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
}
