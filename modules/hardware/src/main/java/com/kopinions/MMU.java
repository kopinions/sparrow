package com.kopinions;

import static java.util.stream.Collectors.toList;

import com.kopinions.core.Bus;
import com.kopinions.core.CPU;
import com.kopinions.core.Data;
import com.kopinions.core.Pageable;
import com.kopinions.core.Registry.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MMU {

  public CPU cpu;
  public Bus bus;
  private List<PTE> mapping;

  public MMU(CPU cpu, Bus bus) {
    this.cpu = cpu;
    this.bus = bus;
    mapping = new ArrayList<>();
  }

  public synchronized Address translate(Address va) {
    this.mapping = IntStream.range(0, 128).mapToObj(
        i -> bus.read(new Address(cpu.registry().get(Name.CR3) + i * 2)))
        .map(PTE::new)
        .collect(toList());

    PTE entry = entry(va);
    return new Address((entry.ppn << 9) + offset(va));
  }

  public int page(Address va) {
    return va.range(15, 9);
  }

  public int offset(Address va) {
    return va.range(8, 0);
  }

  public PTE entry(Address va) {
    int page = page(va);
    if (page < mapping.size()) {
      return mapping.get(page);
    } else {
      return null;
    }
  }

  public Pageable<Data> find(Address address) {
    return null;
  }

  public void update(List<PTE> ptes) {
    mapping = ptes;
  }

  public static class PTE {

    boolean valid;
    short ppn;

    public PTE(short value) {
      this.valid = (value & 0x1) > 0;
      this.ppn = (short) ((value & 0xFFFE) >> 9);
    }
  }
}
