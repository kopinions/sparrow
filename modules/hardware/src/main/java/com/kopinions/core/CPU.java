package com.kopinions.core;

import com.kopinions.MMU;

public interface CPU {
  void tick();

  Interrupter interrupter();

  void execute(Instruction instruction);

  void poweron();

  MMU mmu();

  interface Interrupter {
    enum Type {
      RTC, PGFAULT, SWITCH;
    }
    void interrupt(Type type);

    void on(Type type, Runnable isr);
  }

  Registry registry();
}
