package com.kopinions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.kopinions.core.Memory;
import org.junit.jupiter.api.Test;

class DRAMTest {

  @Test
  public void should_able_to_fetch_content_from_memory() {
    Memory dram = new DRAM(32*1024);
    Address address = new Address(0x0000);
    dram.write(address, (short) 1);
    short data = dram.read(address);
    assertThat(data, is((short)1));
  }
}
