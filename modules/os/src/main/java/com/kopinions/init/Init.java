package com.kopinions.init;

import com.kopinions.apps.monitors.Monitor;
import com.kopinions.apps.monitors.Monitor.JsonChangeSet;
import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Disk;
import com.kopinions.core.Memory;
import com.kopinions.fs.FS.File;
import com.kopinions.fs.DevHDD;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.Kernel;
import com.kopinions.kernel.Proc;
import com.kopinions.kernel.ProcManager;
import com.kopinions.kernel.Reporter;
import com.kopinions.mm.PMM;
import com.kopinions.mm.PageBasePMM;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

public class Init implements Runnable {

  private final Memory memory;
  private final Disk disk;
  private CPU cpu;

  public Init(CPU cpu, Memory memory, Disk disk) {
    this.memory = memory;
    this.disk = disk;
    Interrupter interrupter = cpu.interrupter();
    this.cpu = cpu;
    interrupter.on(Type.RTC, new Runnable() {
      @Override
      public void run() {

      }
    });
  }

  @Override
  public void run() {
    memory.memset(0, memory.size(), (byte) 0);
    // init the page descriptor
    // mov location, cr3

    // idleproc
    Monitor target = new Monitor();

    Reporter<Map<String, Object>> reporter = message -> {
      target.apply(new JsonChangeSet("disk", message));
    };

    new Thread(target).start();
    JobManager jobManager = new JobManager();

    DevHDD hdd = new DevHDD(disk);
    File open = hdd.open(Kernel.HDD_SWAP_SIZE + Kernel.HDD_SYS_SIZE);
    byte[] read = open.read(512);
    int location;
    int size;
    for (int i = 0; i < 64; i++) {
      ByteBuffer byteBuffer = ByteBuffer
          .wrap(new byte[]{
              read[i * 8],
              read[i * 8 + 1],
              read[i * 8 + 2],
              read[i * 8 + 3],
              read[i * 8 + 4],
              read[i * 8 + 5],
              read[i * 8 + 6],
              read[i * 8 + 7]});
      location = byteBuffer.getInt(0);
      size = byteBuffer.getInt(1);
      if (location == 0 || size == 0) {
        continue;
      }
      File job = hdd.open(location);
      byte[] jobdata = job.read(size);
      jobManager.load(ByteBuffer.wrap(jobdata).toString());
    }
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
