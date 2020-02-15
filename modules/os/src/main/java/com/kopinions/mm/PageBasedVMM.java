package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.kernel.SwapManager;
import com.kopinions.mm.Page.PageDirectory;

public class PageBasedVMM implements VMM {

  private SwapManager sm;
  int pgdir;
  private PMM pmm;
  private PageDirectory directory;

  public PageBasedVMM(PMM pmm, SwapManager sm, int pgdir) {
    this.pmm = pmm;
    this.sm = sm;
    this.pgdir = pgdir;
  }

  @Override
  public PageDirectory pgdir() {
    if (directory == null) {
       directory = new PageDirectory(pmm, pgdir);
    }
    return directory;
  }

  @Override
  public void pgfault(Address va) {
    sm.out(this, 1);
    sm.in(this, va);
  }
}
