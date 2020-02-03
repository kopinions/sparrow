package com.kopinions;

import static java.nio.ByteBuffer.allocate;

import com.kopinions.Address.Range;
import com.kopinions.core.Bus;
import com.kopinions.core.Memory;
import java.nio.ByteBuffer;

public class DRAM implements Memory {

  private Bus bus;
  byte[] datas;
  private int size;

  public DRAM(int size) {
    datas = new byte[size];
    this.size = size;
  }

  @Override
  public void attached(Bus bus) {
    this.bus = bus;
  }


  @Override
  public void write(Address address, short data) {
    Address aligned = address.aligned();
    ByteBuffer bytes = allocate(2).putShort(data);
    this.datas[aligned.addr] = bytes.get(0);
    this.datas[aligned.addr+1] = bytes.get(1);
  }

  @Override
  public Range range() {
    return new Range(0, size - 1);
  }

  @Override
  public short read(Address address) {
    Address aligned = address.aligned();
    return allocate(2)
        .put(0, datas[aligned.addr])
        .put(1, datas[aligned.addr + 1])
        .getShort();
  }

  @Override
  public void memset(int from, int to, byte value) {
    for (int i = from; i < to; i++) {
      this.datas[i] = value;
    }
  }

  @Override
  public int size() {
    return size;
  }
}
