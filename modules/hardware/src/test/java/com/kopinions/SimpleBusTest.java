package com.kopinions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.core.Bus;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class SimpleBusTest {
  @Test
  public void should_able_to_read_from_component() {
    Bus bus = new SimpleBus(16);
    HDD hdd = new HDD(1024 * 1024);
    bus.attach(hdd);
    hdd.write(new Address(0x9FFF), (short) 2);
    DRAM dram = new DRAM(32 * 1024);
    dram.write(new Address(0x0FFF), (short)1);
    bus.attach(dram);
    assertThat(bus.read(new Address(0x0FFF)), is((short)1));
    assertEquals(bus.read(new Address(0x9FFF)), (short)2);
  }
}
