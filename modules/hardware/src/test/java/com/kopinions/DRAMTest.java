package com.kopinions;

import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.core.Data;
import com.kopinions.core.Memory;
import org.junit.jupiter.api.Test;

class DRAMTest {

  @Test
  public void should_able_to_fetch_content_from_memory() {
    Memory dram = new DRAM(32*1024);
    dram.memset(0, 32 * 1024, (byte) 0xFF);
    Data data = dram.read(new Address(0x0000));
    byte[] binary = data.binary();
    assertEquals(binary.length, 2);
    assertEquals(binary[0], (byte)0xFF);
    assertEquals(binary[1], (byte)0xFF);
  }
}
