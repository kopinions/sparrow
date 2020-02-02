package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Memory;
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
    MMU mmu = new MMU();
    SISD sisd = new SISD(mmu, bus);
    sisd.execute(new MoveInstruction("cr3", (short) 0));
    Address va = new Address(0x02FF);
    Address pa = mmu.translate(va);
    Assertions.assertEquals(pa, new Address(0x06FF));
  }
}
