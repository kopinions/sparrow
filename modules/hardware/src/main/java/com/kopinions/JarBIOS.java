package com.kopinions;

import com.kopinions.core.BIOS;
import com.kopinions.core.CPU;
import com.kopinions.core.Disk.Section;
import java.lang.reflect.InvocationTargetException;

public class JarBIOS implements BIOS {

  @Override
  public void powerup() {
    CPU cpu = new SISD();
    cpu.poweron();
    HDD hdd = new HDD();
    Section zero = hdd.load(0);
    // load jar
    // thread run
    try {
      Class<?> aClass = Class.forName("com.kopinions.boot.Bootmain");
      Object o = aClass.getDeclaredConstructor().newInstance();
      ((Runnable) o).run();
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      System.err.println("error in boot");
      System.exit(1);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    new JarBIOS().powerup();
  }
}
