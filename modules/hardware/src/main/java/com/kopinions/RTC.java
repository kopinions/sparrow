package com.kopinions;

import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Timer;
import java.util.Date;

public class RTC {

  private Timer timer;
  private CPU.Interrupter interrupter;

  public RTC(Timer timer, CPU.Interrupter interrupter) {
    this.timer = timer;
    this.interrupter = interrupter;
  }

  public void poweron() {
    timer.schedule(() -> interrupter.interrupt(Type.RTC), new Date(), 1000);
  }
}
