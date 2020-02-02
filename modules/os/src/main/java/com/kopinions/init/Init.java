package com.kopinions.init;

import com.kopinions.apps.monitors.Monitor;
import com.kopinions.apps.monitors.Monitor.JsonChangeSet;
import com.kopinions.core.CPU.Interrupter;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Memory;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.Proc;
import com.kopinions.kernel.ProcManager;
import com.kopinions.kernel.Reporter;
import com.kopinions.mm.PMM;
import com.kopinions.mm.PageBasePMM;
import java.util.ArrayList;
import java.util.Map;

public class Init implements Runnable {
  private final Memory memory;
  private final Interrupter interrupter;

  public Init(Memory memory, Interrupter interrupter) {
    this.memory = memory;
    this.interrupter = interrupter;
    interrupter.on(Type.RTC, new Runnable() {
      @Override
      public void run() {

      }
    });
  }

  @Override
  public void run() {
    memory.memset(0, memory.size(), (byte) 0);

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

    PMM pmm = new PageBasePMM();
  }
}
