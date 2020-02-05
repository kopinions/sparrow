package com.kopinions;

import static java.lang.Class.forName;

import com.kopinions.core.CPU.Interrupter;
import com.kopinions.core.BIOS;
import com.kopinions.core.Bus;
import com.kopinions.core.CPU;
import com.kopinions.core.Disk;
import com.kopinions.core.Memory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JarBIOS implements BIOS {

  private final CPU cpu;
  private final Bus bus;
  private final Memory mem;
  private final Interrupter interrupter;
  private Disk disk;
  private final RTC rtc;

  public JarBIOS() {
    bus = new SimpleBus(16);
    MMU mmu = new MMU();
    cpu = new SISD(mmu, bus);
    interrupter = cpu.interrupter();
    rtc = new RTC(new Oscillator(), cpu.interrupter());
    cpu.poweron();
    rtc.poweron();
    disk = HDD.from("hdd.img");
    bus.attach(disk);
    mem = new DRAM(32 * 1024);
    bus.attach(mem);
  }

  @Override
  public void poweron() {
    new Thread(() -> {
      while (true) {
        cpu.tick();
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    try {
      String bootClass = "com.kopinions.boot.Bootmain";
      Constructor<?> constructor = forName(bootClass)
          .getDeclaredConstructor(CPU.class, Memory.class, Disk.class);
      ((Runnable) constructor.newInstance(cpu, mem, disk)).run();
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      System.err.println("error in boot");
      System.exit(1);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    new JarBIOS().poweron();
  }
}
