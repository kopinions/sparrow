package com.kopinions;

import static java.lang.Class.forName;

import com.kopinions.core.BIOS;
import com.kopinions.core.CPU;
import java.lang.reflect.InvocationTargetException;

public class JarBIOS implements BIOS {

  @Override
  public void poweron() {
    CPU cpu = new SISD();
    cpu.poweron();
    HDD hdd = new HDD();
    try {
      String bootClass = "com.kopinions.boot.Bootmain";
      ((Runnable) forName(bootClass).getDeclaredConstructor().newInstance()).run();
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
