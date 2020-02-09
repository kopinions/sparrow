package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.kernel.Kernel;
import java.util.Objects;

public class Page {

  private byte[] data;

  public void setData(byte[] data) {
    this.data = data;
  }

  public int pra_vaddr() {
    return 0;
  }

  enum Status {
    USING,
    FREE
  }

  int index;
  private PMM pmm;
  int size;
  Status status;

  public Page(int index, PMM pmm) {
    this.index = index;
    this.pmm = pmm;
    status = Status.FREE;
  }

  public boolean is(Status expected) {
    return status == expected;
  }

  public int size() {
    return size;
  }

  public int pa() {
    int ppn = this.index;
    return (ppn << Kernel.PGSHIFT) + pmm.start();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Page page = (Page) o;
    return index == page.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index);
  }

  public byte[] data() {
    return data;
  }

  public static class PageDirectory {

    private PMM pmm;

    public PageDirectory(PMM pmm) {
      this.pmm = pmm;
    }

    public static class PageDirectoryEntry {

    }

    public Page alloc(Address address) {
      Page alloc = pmm.alloc();
      // add page table entry to the page directory
      return alloc;
    }
  }

  public static class PageTable {
    public static class PageTableEntry {

    }

    PageTable from(Address address) {
      return null;
    }
  }
}
