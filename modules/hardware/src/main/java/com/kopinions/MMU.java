package com.kopinions;

import com.kopinions.core.Data;
import com.kopinions.core.Pageable;
import java.util.List;

public class MMU {
  private List<PTE> mapping;

  public MMU() {
  }

  public Address translate(Address va) {
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
    return mapping.get(page(va));
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
      this.valid = (value & 0x8000)>0;
      this.ppn = (short) (value & ((1<<12)-1));
    }
  }
}
