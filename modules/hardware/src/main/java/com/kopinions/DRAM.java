package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Data;
import com.kopinions.core.Memory;

public class DRAM implements Memory {

  private Bus bus;
  byte[] datas;
  private int size;

  public DRAM(int size, Bus bus) {
    datas = new byte[size];
    this.size = size;
  }

  @Override
  public void attached(Bus bus) {
    this.bus = bus;
  }

  @Override
  public Data recv() {
    return null;
  }

  @Override
  public void send(Data data) {

  }

  @Override
  public Data at(Address address) {
    Address aligned = address.aligned();
    byte higher = datas[aligned.addr];
    byte lower = datas[aligned.addr+1];
    return new Word((short) (higher<<8 | lower));
  }

  @Override
  public void memset(int from, int to, byte value) {
    for (int i = from; i < to; i++) {
      this.datas[i] = value;
    }
  }
}
