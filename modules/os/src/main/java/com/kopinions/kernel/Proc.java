package com.kopinions.kernel;

import com.kopinions.mm.VMM;
import java.util.Queue;

public class Proc {

  enum State {
    UNINIT,  // uninitialized
    SLEEPING,    // sleeping
    RUNNABLE,    // runnable(maybe running)
    ZOMBIE,      // almost dead, and wait parent proc to reclaim his resource
  }

  static class Context {
    short eip;
    short esp;
    short ebx;
    short ecx;
    short edx;
    short esi;
    short edi;
    short ebp;
  }

  State state;
  int pid;
  int runs;
  VMM vmm;
  Context context;
  String name;
  int exitCode;
  Queue<Proc> processes;


  Proc() {

  }

  void killed() {

  }

  void blocked() {

  }

  void awakened() {

  }
}
