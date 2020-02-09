package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Memory;
import com.kopinions.core.Registry.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MMUTest {
  @Test
  public void should_able_translate_the_va_to_pa() {
    Bus bus = new SimpleBus(16);
    Memory memory = new DRAM(32 * 1024);
    memory.memset(2, 3, (byte)0x80);
    memory.memset(3, 4, (byte)0x03);
    bus.attach(memory);
    SISD sisd = new SISD(bus);
    sisd.registry().set(Name.CR3, (short) 0);
    Address va = new Address(0x02FF);
    Address pa = sisd.mmu().translate(va);
    Assertions.assertEquals(pa, new Address(0x06FF));
  }
}
