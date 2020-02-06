package com.kopinions.mm;

public class PageBasedVMM implements VMM {
  int pgdir;

  public PageBasedVMM(int pgdir) {
    this.pgdir = pgdir;
  }

  @Override
  public void pgfault() {

  }
}
