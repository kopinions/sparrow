package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Component;
import com.kopinions.core.Data;
import com.kopinions.core.Pageable;

public class MMU implements Component {
  private Bus bus;
  private PTE[] mapping;

  public MMU(Bus bus, PTE[] mapping) {
    this.bus = bus;
    this.mapping = mapping;
    this.bus.attach(this);
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
    return mapping[page(va)];
  }

  public void attached(Bus bus) {

  }

  @Override
  public Data recv() {
    return null;
  }

  @Override
  public void send(Data data) {

  }

  public Data at(Address address) {
    return new PlainData("");
  }

  public Pageable<Data> find(Address address) {
    return null;
  }

  public static class PTE {
    boolean valid;
    int ppn;

    public PTE(boolean valid, int ppn) {
      this.valid = valid;
      this.ppn = ppn;
    }
  }
}
