package com.kopinions;

import com.kopinions.core.Timer;
import java.util.Date;
import java.util.TimerTask;

public class Oscillator implements Timer {

  private final java.util.Timer timer;

  public Oscillator() {
    timer = new java.util.Timer();
  }

  @Override
  public void schedule(Task task, Date start, long period) {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        task.run();
      }
    }, start, period);
  }
}
