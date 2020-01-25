package com.kopinions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kopinions.core.Command;
import com.kopinions.core.Decoder;
import org.junit.jupiter.api.Test;

class MMUTest {
  @Test
  public void should_able_to_get_command() {
    MMU mmu = new MMU();
    Address address = new Address("0x00");
    RawInst inst = mmu.at(address);
    assertNotNull(inst);
  }
}
