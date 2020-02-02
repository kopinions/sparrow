package com.kopinions;

public class Address {

  int addr;
  public static final int WIDTH = 16;

  public Address(int addr) {
    this.addr = addr;
  }

  public int range(int high, int low) {
    int high_mask = 0xFFFF >> (WIDTH - 1 - high);
    int low_mask = 0xFFFF << low;
    return ((high_mask & this.addr) & (low_mask & this.addr)) >> low;
  }

  public Address aligned() {
    return new Address(addr & 0xFFFE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return addr == address.addr;
  }

  public static class Range {
    int start;
    int end;

    public Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    boolean contains(Address address) {
      return address.addr >= start && address.addr <= end;
    }
  }
}
