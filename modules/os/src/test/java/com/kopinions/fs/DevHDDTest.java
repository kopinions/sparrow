package com.kopinions.fs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.kopinions.HDD;
import com.kopinions.fs.FS.File;
import com.kopinions.kernel.Kernel;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class DevHDDTest {

  @Test
  public void should_able_to_read_the_file_system_and_file() {
    HDD hdd = new HDD(Kernel.HDD_SIZE);
    for (int i = 0; i < 64; i++) {
      ByteBuffer allocate = ByteBuffer.allocate(8);
      allocate.putInt(0, Kernel.HDD_FS_ROOT + i * 512);
      allocate.putInt(4, 2);

      for (int j = 0; j < 4; j++) {
        hdd.write(Kernel.HDD_FS_ROOT + i * 8 + j * 2, allocate.getShort(j * 2));
      }
      hdd.write(Kernel.HDD_FS_ROOT + i * 512, (short) 1);
    }

    DevHDD devHDD = new DevHDD(hdd);
    File root = devHDD.root();
    int location, size;
    byte[] read = root.read(512);
    for (int i = 0; i < 64; i++) {
      ByteBuffer byteBuffer = ByteBuffer
          .wrap(new byte[]{
              read[i * 8],
              read[i * 8 + 1],
              read[i * 8 + 2],
              read[i * 8 + 3],
              read[i * 8 + 4],
              read[i * 8 + 5],
              read[i * 8 + 6],
              read[i * 8 + 7]});
      location = byteBuffer.getInt(0);
      size = byteBuffer.getInt(4);
      assertThat(size, equalTo(2));
      assertThat(location, equalTo(Kernel.HDD_FS_ROOT + i * 512));

      File job = devHDD.open(location);
      byte[] jobdata = job.read(size);
      assertThat(ByteBuffer.wrap(jobdata).getShort(), equalTo((short) 1));
    }
  }
}
