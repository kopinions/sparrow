package com.kopinions;

import com.kopinions.core.Instruction;
import com.kopinions.core.Data;
import com.kopinions.core.Decoder;

public class ISADecoder implements Decoder<Short> {

  @Override
  public Instruction decode(Short inst) {
    if (inst == 0xFF) {
      return new ReturnInstruction();
    } else {
      return new InterruptInstruction();
    }
  }
}
