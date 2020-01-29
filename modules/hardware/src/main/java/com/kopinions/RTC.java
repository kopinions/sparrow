package com.kopinions;

import com.kopinions.Interrupter.Type;
import com.kopinions.Timer.Task;
import java.util.Date;

public class RTC {

  private Timer timer;
  private Interrupter interrupter;

  public RTC(Timer timer, Interrupter interrupter) {
    this.timer = timer;
    this.interrupter = interrupter;
  }

  public void start() {
    timer.schedule(() -> interrupter.interrupt(Type.RTC), new Date(), 1000);
  }
}
