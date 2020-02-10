package com.kopinions.mm;

import static com.kopinions.mm.Page.Status.FREE;
import static com.kopinions.mm.Page.Status.USING;
import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toList;

import com.kopinions.Address;
import com.kopinions.core.Memory;
import com.kopinions.kernel.Kernel;
import com.kopinions.mm.Page.PageDirectory;
import com.kopinions.mm.Page.PageTable.PageTableEntry;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.IntStream;

public class PageBasePMM implements PMM {

  private Memory memory;
  private final int size;
  private final int pagesize;
  List<Page> pages;

  public PageBasePMM(Memory memory, int size, int pagesize) {
    this.memory = memory;
    this.size = size;
    this.pagesize = pagesize;
    pages = IntStream.range(0, size / pagesize)
        .mapToObj(zone_num -> new Page(memory, zone_num, this)).collect(toList());
  }

  @Override
  public List<Page> alloc(int request) {
    List<Page> free = pages.stream().filter(p -> p.is(FREE)).limit(request).collect(toList());
    if (free.size() < request) {
      throw new RuntimeException("No enough memory");
    }
    return free.stream()
        .map(p -> {
          p.status = USING;
          return p;
        }).collect(toList());
  }

  @Override
  public Page alloc() {
    Page find = pages.stream().filter(p -> p.is(FREE)).findFirst()
        .orElseThrow(() -> new RuntimeException("No enough memory"));
    find.status = USING;
    return find;
  }

  @Override
  public void free(List<Page> pages) {
    pages.forEach(p -> p.status = FREE);
  }

  @Override
  public void free(Page page) {
    page.status = FREE;
  }

  @Override
  public int available() {
    return toIntExact(pages.stream().filter(p -> p.is(FREE)).count());
  }

  @Override
  public int ppn(Page page) {
    return pages.indexOf(page);
  }

  @Override
  public Page from(short la) {
    short ppn = (short) ((la & 0x3FFF) >> 9);
    return pages.get(ppn);
  }

  @Override
  public int start() {
    return Kernel.MEM_USERSPACE_SIZE;
  }

  @Override
  public PageTableEntry pde(Address pgdir, short la) {
    return null;
  }

  @Override
  public PageTableEntry pte(PageDirectory pgdir, Address addr) {
    return null;
  }

  @Override
  public void write(int address, byte[] data) {
    ByteBuffer wrap = ByteBuffer.wrap(data);
    for (int i = 0; i < data.length / 2; i++) {
      Address address1 = new Address(i * 2 + address);
      memory.write(address1, wrap.getShort(i * 2));
    }
  }

  @Override
  public void pageInsert(int pgdir, Page alloc, Address address) {
    int pdx = address.pdx();
    int ptx = address.ptx();
    memory.write(new Address(pgdir + pdx * 2 + ptx * 2), (short) alloc.pa());
  }
}
