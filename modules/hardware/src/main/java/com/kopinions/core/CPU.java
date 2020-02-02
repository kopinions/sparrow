package com.kopinions.core;

public interface CPU {
  void tick();

  Interrupter interrupter();

  void execute(Instruction instruction);

  void poweron();


  interface Interrupter {
    enum Type {
      RTC
    }
    void interrupt(Type type);

    void on(Type type, Runnable isr);
  }
}
