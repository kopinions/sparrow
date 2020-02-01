package com.kopinions.init;

import com.kopinions.apps.monitors.Monitor;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.StringReporter;

public class Init implements Runnable {

  @Override
  public void run() {
    new Thread(new Monitor()).start();
    JobManager jobManager = new JobManager();
    jobManager.report(new StringReporter());
  }
}
