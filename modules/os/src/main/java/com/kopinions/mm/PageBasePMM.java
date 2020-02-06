package com.kopinions.mm;

import static com.kopinions.mm.Page.Status.FREE;
import static com.kopinions.mm.Page.Status.USING;
import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toList;

import com.kopinions.kernel.Kernel;
import java.util.List;
import java.util.stream.IntStream;

public class PageBasePMM implements PMM {

  private static PMM pmm = null;
  private final int size;
  private final int pagesize;
  List<Page> pages;

  protected PageBasePMM(int size, int pagesize) {
    this.size = size;
    this.pagesize = pagesize;
    pages = IntStream.range(0, size / pagesize).mapToObj(zone_num -> new Page(zone_num, this)).collect(toList());
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

  public static synchronized PMM instance(int size, int pagesize) {
    if (pmm == null) {
      pmm = new PageBasePMM(size, pagesize);
    }
    return pmm;
  }
}
