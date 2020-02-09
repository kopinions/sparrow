package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.core.Memory;
import com.kopinions.kernel.Kernel;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

  private Memory memory;
  int index;
  private PMM pmm;
  int size;
  Status status;

  public Page(Memory memory, int index, PMM pmm) {
    this.memory = memory;
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
    List<Short> data = IntStream.range(0, 256).mapToObj(i -> memory.read(new Address(pa())))
        .collect(Collectors.toList());
    ByteBuffer allocate = ByteBuffer.allocate(512);
    IntStream.range(0, 256).forEach(i -> allocate.putShort(i*2, data.get(i)));
    return allocate.array();
  }

  public static class PageDirectory {

    private PMM pmm;
    private int pgdir;

    public PageDirectory(PMM pmm, int pgdir) {
      this.pmm = pmm;
      this.pgdir = pgdir;
    }

    public static class PageDirectoryEntry {

    }

    public Page alloc(Address address) {
      Page alloc = pmm.alloc();
      pmm.pageInsert(pgdir, alloc, address);
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
