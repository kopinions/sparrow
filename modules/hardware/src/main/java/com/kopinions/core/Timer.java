package com.kopinions.core;

import java.util.Date;

public interface Timer {
  interface Task extends Runnable {

  }

  void schedule(Task task, Date start, long period);
}
