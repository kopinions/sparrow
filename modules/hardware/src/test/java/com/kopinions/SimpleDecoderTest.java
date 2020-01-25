package com.kopinions;

import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.core.Command;
import org.junit.jupiter.api.Test;

class SimpleDecoderTest {

  @Test
  public void should_able_to_decode_the_raw_inst() {
    RawInst rawInst = new RawInst("0x00");
    SimpleDecoder simpleDecoder = new SimpleDecoder();
    Command decode = simpleDecoder.decode(rawInst);
    assertNotNull(decode);
  }
}
