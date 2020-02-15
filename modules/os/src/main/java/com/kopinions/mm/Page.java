package com.kopinions.mm;

import static java.util.stream.IntStream.range;

import com.kopinions.Address;
import com.kopinions.core.Memory;
import com.kopinions.kernel.Kernel;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Page {

  private byte[] data;

  public void setData(byte[] data) {
    ByteBuffer wrap = ByteBuffer.wrap(data);
    range(0, data.length / 2).forEach(i ->
        memory.write(new Address(pa() + i * 2), wrap.getShort(i * 2)));
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

  public int ppn() {
    return index;
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
    List<Short> data = range(0, 256).mapToObj(i -> memory.read(new Address(pa())))
        .collect(Collectors.toList());
    ByteBuffer allocate = ByteBuffer.allocate(512);
    range(0, 256).forEach(i -> allocate.putShort(i * 2, data.get(i)));
    return allocate.array();
  }

  public static class PageDirectory {

    private PMM pmm;
    private int pgdir;
    private List<Page> allocated;

    public PageDirectory(PMM pmm, int pgdir) {
      this.pmm = pmm;
      this.pgdir = pgdir;
      allocated = new ArrayList<>();
    }

    public short as() {
      return (short) pgdir;
    }

    public void free(Address address) {
      allocated.forEach(p -> pmm.free(p));
      allocated = new ArrayList<>();
      pmm.pgremove(pgdir);
    }

    public static class PageDirectoryEntry {

    }

    public Page alloc(Address address) {
      Page alloc = pmm.alloc();
      pmm.pageInsert(pgdir, alloc, address);
      allocated.add(alloc);
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
