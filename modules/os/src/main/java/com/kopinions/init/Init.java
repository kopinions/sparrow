package com.kopinions.init;

import com.kopinions.apps.monitors.Monitor;
import com.kopinions.apps.monitors.Monitor.JsonChangeSet;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.ProcManager;
import com.kopinions.kernel.Reporter;
import java.util.HashMap;
import java.util.Map;

public class Init implements Runnable {

  @Override
  public void run() {
    Monitor target = new Monitor();

    Reporter<Map<String, Object>> reporter = message -> {
      target.apply(new JsonChangeSet("disk", message));
    };
    new Thread(target).start();

    JobManager jobManager = new JobManager();
    ProcManager procManager = new ProcManager();
    jobManager.report(reporter);
  }
}
