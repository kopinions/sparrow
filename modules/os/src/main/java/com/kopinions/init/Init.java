package com.kopinions.init;

import static java.util.Arrays.asList;

import com.kopinions.Address;
import com.kopinions.apps.monitors.Monitor;
import com.kopinions.apps.monitors.Monitor.JsonChangeSet;
import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Disk;
import com.kopinions.core.Memory;
import com.kopinions.core.Registry.Name;
import com.kopinions.fs.DevHDD;
import com.kopinions.fs.FS.File;
import com.kopinions.kernel.FIFOSwap;
import com.kopinions.kernel.Job;
import com.kopinions.kernel.JobManager;
import com.kopinions.kernel.Kernel;
import com.kopinions.kernel.Proc;
import com.kopinions.kernel.ProcManager;
import com.kopinions.kernel.Report;
import com.kopinions.kernel.Reporter;
import com.kopinions.kernel.Selector;
import com.kopinions.kernel.SwapManager;
import com.kopinions.mm.PMM;
import com.kopinions.mm.PageBasePMM;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Init implements Runnable {

  private final Memory memory;
  private final Disk disk;
  private CPU cpu;
  private Monitor monitor;
  private JobManager jobManager;
  private DevHDD hdd;
  private PMM pmm;
  private SwapManager sm;
  private ProcManager procManager;

  public Init(CPU cpu, Memory memory, Disk disk) {
    this.memory = memory;
    this.disk = disk;
    this.cpu = cpu;

    monitor = new Monitor();
    jobManager = new JobManager();
    hdd = new DevHDD(disk);
    pmm = new PageBasePMM(memory, Kernel.MEM_USERSPACE_SIZE, Kernel.PAGE_SIZE);
    sm = new FIFOSwap(disk, pmm);
    procManager = new ProcManager(elements -> new ArrayList<>() {{
      Proc proc = elements.peek();
      if (proc != null) {
        add(proc);
      }
    }}, cpu, pmm, sm);
  }

  @Override
  public void run() {
    memory.memset(0, memory.size(), (byte) 0);
    new Thread(monitor).start();
    loadJobs();

    cpu.interrupter().on(Type.RTC, () -> {
      if (procManager.current().scheduleNeeded()) {
        try {
          Proc select = procManager.select(elements -> asList(elements.poll()));
          if (select != null) {
            procManager.active(select);
          } else {
            Selector<Job> FIFOSelector = elements -> asList(elements.poll());
            Optional<Job> single = jobManager.single(FIFOSelector);
            single.ifPresent(job -> procManager.create(job));
          }

          procManager.schedule();
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      procManager.tick();
    });

    cpu.interrupter().on(Type.RTC, () -> {
      procManager.report(message -> monitor.apply(new JsonChangeSet("proc", message)));
      jobManager.report(message -> monitor.apply(new JsonChangeSet("job", message)));
      hdd.report(message -> monitor.apply(new JsonChangeSet("disk_info", message)));
      ((Report<Map<String, Object>>) pmm)
          .report(message -> monitor.apply(new JsonChangeSet("mem_info", message)));
    });
    cpu.interrupter().on(Type.PGFAULT, () -> {
      short va = cpu.registry().get(Name.CR2);
      procManager.current().vmm().pgfault(new Address(va));
    });
    cpu.interrupter().on(Type.SWITCH, () -> {
      short pid = cpu.registry().get(Name.EDI);
      procManager.exit(procManager.id(pid));
    });
  }

  private void loadJobs() {
    File open = hdd.open(Kernel.HDD_SWAP_SIZE + Kernel.HDD_SYS_SIZE);
    byte[] read = open.read(512);
    ByteBuffer readdata = ByteBuffer.wrap(read);
    int location;
    int size;
    for (int i = 0; i < 64; i++) {
      location = readdata.getInt(0);
      size = readdata.getInt(4);
      if (location == 0 || size == 0) {
        continue;
      }
      File job = hdd.open(location);
      byte[] jobdata = job.read(size);
      jobManager.create(location, size, jobdata);
      job.close();
    }
  }
}
