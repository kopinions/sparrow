package com.kopinions;

import static org.hamcrest.CoreMatchers.is;

import com.kopinions.kernel.Kernel;
import java.nio.ByteBuffer;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class HDDTest {

  @Test
  void should_able_to_read_the_written() {
    HDD hdd = new HDD(Kernel.HDD_SIZE);
    ByteBuffer allocate = ByteBuffer.allocate(8);
    allocate.putInt(0, Kernel.HDD_FS_ROOT);
    for (int j = 0; j < 2; j++) {
      hdd.write(Kernel.HDD_FS_ROOT + j * 2, allocate.getShort(j * 2));
    }
    ByteBuffer readedInt = ByteBuffer.allocate(4);

    for (int j = 0; j < 2; j++) {
      short read = hdd.read(Kernel.HDD_FS_ROOT + j * 2);
      readedInt.putShort(j * 2, read);
    }
    MatcherAssert.assertThat(readedInt.getInt(), is(Kernel.HDD_FS_ROOT));
  }
}
