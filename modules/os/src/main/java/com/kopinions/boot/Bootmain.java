package com.kopinions.boot;

import com.kopinions.init.Init;

public class Bootmain implements Runnable {
  @Override
  public void run() {
    new Init().run();
  }
}
