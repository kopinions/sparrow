package com.kopinions;

import com.kopinions.Address.Range;
import com.kopinions.core.Bus;
import com.kopinions.core.Data;
import com.kopinions.core.Disk;

public class HDD implements Disk {

  private int size;

  public static HDD from(String img) {
    return new HDD(1024*1024);
  }

  public HDD(int size) {

    this.size = size;
  }

  @Override
  public Section load(int i) {
    return null;
  }

  @Override
  public void attached(Bus bus) {

  }

  @Override
  public Data read(Address pa) {
    return null;
  }

  @Override
  public void write(Address address, Data data) {

  }

  @Override
  public Range range() {
    return new Range(0x8000, 0xFFFFFF);
  }
}
