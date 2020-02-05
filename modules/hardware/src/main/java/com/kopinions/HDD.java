package com.kopinions;

import static java.nio.ByteBuffer.wrap;

import com.kopinions.Address.Range;
import com.kopinions.core.Bus;
import com.kopinions.core.Disk;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

public class HDD implements Disk {

  private int size;
  private byte[] cells;

  public static HDD from(String img) {
    HDD hdd = new HDD(1024 * 1024);
    URL resource = hdd.getClass().getClassLoader().getResource(img);
    try {
      FileInputStream fileInputStream = new FileInputStream(new java.io.File(resource.toURI()));
      hdd.cells = fileInputStream.readAllBytes();
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }
    return hdd;
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
