package com.kopinions;

import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.core.Instruction;
import com.kopinions.core.Data;
import org.junit.jupiter.api.Test;

class ISADecoderTest {

  @Test
  public void should_able_to_decode_the_raw_inst() {
    Data data = new Word((short) 0xFFFF);
    ISADecoder decoder = new ISADecoder();
    Instruction decode = decoder.decode(data);
    assertNotNull(decode);
  }
}
