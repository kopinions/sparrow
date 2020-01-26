package com.kopinions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kopinions.MMU.PTE;
import com.kopinions.core.Bus;
import com.kopinions.core.Data;
import com.kopinions.core.Memory;
import com.kopinions.core.Pageable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MMUTest {

  @Test
  public void should_able_to_get_command() {
    Bus bus = new SimpleBus(16);
    Memory memory = new DRAM(32 * 1024, bus);
    MMU mmu = new MMU(bus, new PTE[]{});
    Address address = new Address(0x0000);
    Data inst = mmu.at(address);
    assertNotNull(inst);
  }

  @Test
  public void should_able_translate_the_va_to_pa() {
    Bus bus = new SimpleBus(16);
    Memory memory = new DRAM(32 * 1024, bus);
    bus.attach(memory);
    MMU mmu = new MMU(bus, new PTE[]{new PTE(true, 1), new PTE(true, 3)});
    Address va = new Address(0x02FF);
    Address pa = mmu.translate(va);
    Assertions.assertEquals(pa, new Address(0x06FF));
  }
}
