package com.kopinions.boot;

import com.kopinions.core.CPU;
import com.kopinions.core.Disk;
import com.kopinions.core.Memory;
import com.kopinions.init.Init;

public class Bootmain implements Runnable {

  private final Disk disk;
  private Memory memory;
  private CPU cpu;

  public Bootmain(CPU cpu, Memory memory, Disk disk) {
    this.memory = memory;
    this.disk = disk;
    this.cpu = cpu;
  }

  @Override
  public void run() {
    new Init(cpu, memory, disk).run();
  }
}
