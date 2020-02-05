package com.kopinions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.ByteBuffer;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class HDDTest {

  @Test
  void should_able_to_read_the_written() {
    int hddSize = 1024 * 1024;
    HDD hdd = new HDD(hddSize);
    ByteBuffer allocate = ByteBuffer.allocate(8);
    int hddFsRoot = 64 * 1024 + 16 * 1024;

    allocate.putInt(0, hddFsRoot);
    for (int j = 0; j < 2; j++) {
      hdd.write(hddFsRoot + j * 2, allocate.getShort(j * 2));
    }
    ByteBuffer readedInt = ByteBuffer.allocate(4);

    for (int j = 0; j < 2; j++) {
      short read = hdd.read(hddFsRoot + j * 2);
      readedInt.putShort(j * 2, read);
    }
    assertThat(readedInt.getInt(), is(hddFsRoot));
  }

  @Test
  void should_able_to_write_the_job_location_size_and_rehydra() {
    int hddSize = 1024 * 1024;
    HDD hdd = new HDD(hddSize);
    ByteBuffer allocate = ByteBuffer.allocate(8);
    int hddFsRoot = 64 * 1024 + 16 * 1024;

    allocate.putInt(0, hddFsRoot);
    allocate.putInt(4, hddFsRoot);
    for (int j = 0; j < 2; j++) {
      hdd.write(hddFsRoot + j * 2, allocate.getShort(j * 2));
    }
    ByteBuffer readedInt = ByteBuffer.allocate(4);

    for (int j = 0; j < 2; j++) {
      short read = hdd.read(hddFsRoot + j * 2);
      readedInt.putShort(j * 2, read);
    }
    assertThat(readedInt.getInt(), is(hddFsRoot));
  }
}
