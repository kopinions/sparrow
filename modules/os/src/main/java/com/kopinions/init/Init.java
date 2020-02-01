package com.kopinions.init;

import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.StringReporter;

public class Init implements Runnable {

  @Override
  public void run() {
    JobManager jobManager = new JobManager();
    jobManager.report(new StringReporter());
  }
}
