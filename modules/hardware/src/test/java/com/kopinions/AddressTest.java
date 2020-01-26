package com.kopinions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  public void should_able_to_extract_range_of_addr(){
    Address address = new Address(0b0001000100100010);
    int high_byte = address.range(15, 8);
    assertEquals(0b00010001, high_byte);

    int low_byte = address.range(7, 0);
    assertEquals(0b00100010, low_byte);

    int mid_byte = address.range(12, 5);
    assertEquals(0b10001001, mid_byte);
  }
}
