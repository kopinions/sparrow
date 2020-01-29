package com.kopinions;

import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.core.Instruction;
import com.kopinions.core.Data;
import org.junit.jupiter.api.Test;

class SimpleDecoderTest {

  @Test
  public void should_able_to_decode_the_raw_inst() {
    Data data = new Word((short) 0xFFFF);
    SimpleDecoder decoder = new SimpleDecoder();
    Instruction decode = decoder.decode(data);
    assertNotNull(decode);
  }
}
