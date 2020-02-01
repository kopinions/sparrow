package com.kopinions;

import com.kopinions.core.CPU;
import com.kopinions.core.Instruction;

// Single Instruction Single Data
public class SISD implements CPU {
  private short pc;

  @Override
  public void execute(Instruction instruction) {

  }

  @Override
  public void poweron() {
    pc = 0;
  }
}
