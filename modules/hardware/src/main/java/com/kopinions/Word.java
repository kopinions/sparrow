package com.kopinions;

import com.kopinions.core.Data;

public class Word implements Data {

  private short raw;

  public Word(short raw) {
    this.raw = raw;
  }

  @Override
  public byte[] binary() {
    return new byte[]{
        (byte) (raw >> 8),
        (byte) ((raw << 8) >> 8)
    };
  }
}
