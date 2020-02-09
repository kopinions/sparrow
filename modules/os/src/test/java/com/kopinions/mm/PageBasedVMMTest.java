package com.kopinions.mm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.kopinions.Address;
import com.kopinions.DRAM;
import com.kopinions.HDD;
import com.kopinions.core.Disk;
import com.kopinions.kernel.FIFOSwap;
import com.kopinions.kernel.Kernel;
import com.kopinions.kernel.SwapManager;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PageBasedVMMTest {

  @Test
  void should_write_the_new_allocated_page_to_page_directory() {
    Disk disk = new HDD(Kernel.HDD_SIZE);
    DRAM dram = new DRAM(Kernel.MEM_SIZE);
    PMM pmm = new PageBasePMM(dram, Kernel.MEM_SIZE, Kernel.PAGE_SIZE);
    SwapManager sm = new FIFOSwap(disk, pmm);
    Page pageDirectory = pmm.alloc();
    PageBasedVMM vmm = new PageBasedVMM(pmm, sm, pageDirectory.pa());
    Page pageManagementByPageDirectory = vmm.pgdir().alloc(new Address(0));
    // check the page directory is correct
    assertThat(pageManagementByPageDirectory, notNullValue());
    byte[] data = pageDirectory.data();
    short pte = ByteBuffer.wrap(data).getShort(0);
    assertThat(pte, is((short)pageManagementByPageDirectory.pa()));
  }
}
