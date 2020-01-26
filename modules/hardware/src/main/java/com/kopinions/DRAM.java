package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Data;
import com.kopinions.core.Memory;

public class DRAM implements Memory {

  private Bus bus;

  public DRAM(int size, Bus bus) {

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
}
