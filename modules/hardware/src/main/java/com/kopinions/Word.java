package com.kopinions;

import com.kopinions.core.Data;

public class Word implements Data {

  private short raw;

  public Word(short raw) {
    this.raw = raw;
  }

  @Override
  public byte[] binary() {
    return s2b(this.raw);
  }

  public static byte[] s2b(short v) {
    return new byte[]{
        (byte) (v >> 8),
        (byte) ((v << 8) >> 8)
    };
  }

  @Override
  public int size() {
    return 2;
  }
}
