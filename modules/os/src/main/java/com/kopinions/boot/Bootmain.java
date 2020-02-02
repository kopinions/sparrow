package com.kopinions.boot;

import com.kopinions.core.CPU.Interrupter;
import com.kopinions.core.Memory;
import com.kopinions.init.Init;

public class Bootmain implements Runnable {

  private Memory memory;
  private Interrupter interrupter;

  public Bootmain(Memory memory, Interrupter interrupter) {
    this.memory = memory;
    this.interrupter = interrupter;
  }

  @Override
  public void run() {
    new Init(memory, interrupter).run();
  }
}
