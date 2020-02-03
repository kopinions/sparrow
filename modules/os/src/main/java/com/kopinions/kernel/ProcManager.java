package com.kopinions.kernel;

import java.util.PriorityQueue;
import java.util.Queue;

public class ProcManager {

  private Queue<Proc> waiting;
  private Queue<Proc> running;
  static Proc current;
  static Proc idle;
  private Selector<Proc> selector;

  public ProcManager(Selector<Proc> procSelector) {
    selector = procSelector;
    this.running = new PriorityQueue<>();
    this.waiting = new PriorityQueue<>();
    idle = new Proc(0);
    idle.need_resched = true;
    idle.awakened();
    current = idle;
  }

  Proc create(Job job) {
    return null;
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
    if (current.state == Proc.State.RUNNABLE) {
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
