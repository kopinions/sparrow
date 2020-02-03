package com.kopinions;

import static java.nio.ByteBuffer.wrap;

import com.kopinions.Address.Range;
import com.kopinions.core.Bus;
import com.kopinions.core.Disk;
import java.nio.ByteBuffer;

public class HDD implements Disk {

  private int size;
  private byte[] cells;

  public static HDD from(String img) {
    return new HDD(1024 * 1024);
  }

  public HDD(int size) {
    this.size = size;
    cells = new byte[size];
  }

  @Override
  public short read(int address) {
    return wrap(new byte[]{
        cells[address],
        cells[address + 1]
    }).getShort();
  }

  @Override
  public void write(int address, short data) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).putShort(data);
    cells[address] = byteBuffer.get(0);
    cells[address + 1] = byteBuffer.get(1);
  }

  @Override
  public void attached(Bus bus) {

  }

  @Override
  public short read(Address pa) {
    return wrap(new byte[]{
        cells[pa.aligned().addr],
        cells[pa.aligned().addr + 1]
    }).getShort();
  }

  @Override
  public void write(Address address, short data) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).putShort(data);
    cells[address.aligned().addr] = byteBuffer.get(0);
    cells[address.aligned().addr + 1] = byteBuffer.get(1);
  }

  @Override
  public Range range() {
    return new Range(0x8000, 0xFFFFFF);
  }
}
