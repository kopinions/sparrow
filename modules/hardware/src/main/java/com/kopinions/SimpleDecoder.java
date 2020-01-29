package com.kopinions;

import com.kopinions.core.Instruction;
import com.kopinions.core.Data;
import com.kopinions.core.Decoder;

public class SimpleDecoder implements Decoder {

  @Override
  public Instruction decode(Data inst) {
    return new InterruptInstruction();
  }
}
