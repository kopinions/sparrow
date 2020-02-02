package com.kopinions.kernel;

import com.kopinions.mm.VMM;
import java.util.Objects;
import java.util.Queue;

public class Proc implements Comparable<Proc> {

  public boolean scheduleNeeded() {
    return need_resched;
  }

  @Override
  public int compareTo(Proc o) {
    return Integer.compare(this.priority, o.priority);
  }

  enum State {
    CREATED,  // uninitialized
    SLEEPING,    // sleeping
    RUNNABLE,    // runnable(maybe running)
    ZOMBIE,      // almost dead, and wait parent proc to reclaim his resource
  }

  class Context {
    public short eip;
    public short esp;
    public short ebx;
    public short ecx;
    public short edx;
    public short esi;
    public short edi;
    public short ebp;
  }
  State state;
  int pid;
  int runs;
  VMM vmm;
  Context context;
  String name;
  boolean need_resched;
  int exitCode;
  int priority;
  Queue<Proc> processes;


  Proc(int pid) {
    this.pid = pid;
    need_resched = false;
    state = State.CREATED;
    priority = 1;
  }

  void killed() {
  }

  void blocked() {
    this.state = State.SLEEPING;
  }

  void awakened() {
    this.state = State.RUNNABLE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Proc proc = (Proc) o;
    return pid == proc.pid;
  }

  @Override
  public int hashCode() {
    return Objects.hash(pid);
  }
}
