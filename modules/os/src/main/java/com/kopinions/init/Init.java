package com.kopinions.init;

import com.kopinions.apps.monitors.Monitor;
import com.kopinions.apps.monitors.Monitor.JsonChangeSet;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.Proc;
import com.kopinions.kernel.ProcManager;
import com.kopinions.kernel.Reporter;
import com.kopinions.kernel.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Init implements Runnable {

  @Override
  public void run() {
    Monitor target = new Monitor();

    Reporter<Map<String, Object>> reporter = message -> {
      target.apply(new JsonChangeSet("disk", message));
    };
    new Thread(target).start();

    JobManager jobManager = new JobManager();
    ProcManager procManager = new ProcManager(elements -> new ArrayList<>() {{
      Proc proc = elements.peek();
      if (proc != null) {
        add(proc);
      }
    }});
    jobManager.report(reporter);
    new Thread(() -> {
      while (true) {
        if (procManager.current().scheduleNeeded()) {
          try {
            procManager.schedule();
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }
}
