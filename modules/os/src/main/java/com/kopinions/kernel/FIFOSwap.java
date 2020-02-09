package com.kopinions.kernel;

import com.kopinions.Address;
import com.kopinions.core.Disk;
import com.kopinions.fs.FS.File;
import com.kopinions.fs.SwapFS;
import com.kopinions.mm.PMM;
import com.kopinions.mm.Page;
import com.kopinions.mm.Page.PageTable.PageTableEntry;
import com.kopinions.mm.VMM;

public class FIFOSwap implements SwapManager {

  private final SwapFS fs;
  private PMM pmm;

  public FIFOSwap(Disk disk, PMM pmm) {
    fs = new SwapFS(disk);
    this.pmm = pmm;
  }

  @Override
  public void out(VMM vmm, int pages) {

    for (int i = 0; i != pages; ++i) {
      Page page = select(vmm);
      if (page == null) {
        System.out.println("swap_out: call swap_out_victim failed\n");
        break;
      }

      int v = page.pra_vaddr();
      PageTableEntry pte = pmm.pte(null, new Address(v));
      File open = fs.open(page.pra_vaddr());
      open.write(page.data());
      pmm.free(page);
    }
  }

  private Page select(VMM vmm) {
    return null;
  }

  @Override
  public Page in(VMM vmm, Address addr) {
    Page result = pmm.alloc();

    assert (result != null);

    PageTableEntry pte = pmm.pte(vmm.pgdir(), addr);
    File open = fs.open(addr.as());
    byte[] read = open.read(512);
    pmm.write(result.pa(), read);
    return result;
  }
}
