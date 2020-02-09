package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.kernel.SwapManager;
import com.kopinions.mm.Page.PageDirectory;

public class PageBasedVMM implements VMM {

  private SwapManager sm;
  int pgdir;
  private PMM pmm;

  public PageBasedVMM(PMM pmm, SwapManager sm, int pgdir) {
    this.pmm = pmm;
    this.sm = sm;
    this.pgdir = pgdir;
  }

  @Override
  public PageDirectory pgdir() {
    return new PageDirectory(pmm, pgdir);
  }

  @Override
  public void pgfault(Address va) {

  }
}
