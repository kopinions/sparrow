package com.kopinions.tools;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.kopinions.kernel.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Formatter {

  private String disk;

  public Formatter(String disk) {
    this.disk = disk;
  }

  public void format() throws URISyntaxException, IOException {
    URL resource = this.getClass().getClassLoader().getResource(disk);
    FileOutputStream fileOutputStream = new FileOutputStream(new File(resource.toURI()), false);

    byte[] zero = new byte[Kernel.HDD_SIZE];
    Arrays.fill(zero, (byte) 0);
    ByteBuffer image = ByteBuffer.wrap(zero);
    for (int i = 0; i < 64; i++) {
      int jobPosition = Kernel.HDD_FS_ROOT + (i+1) * Kernel.PAGE_SIZE;
      int jobSize = Kernel.PAGE_SIZE;
      image.putInt(Kernel.HDD_FS_ROOT + i * 8, jobPosition);
      image.putInt(Kernel.HDD_FS_ROOT + i * 8 + 4, jobSize);

      int INSTRUCTION_SIZE = 252;
      image.putInt(jobPosition, 1000);
      image.putInt(jobPosition + 4, INSTRUCTION_SIZE);
      Random random = new Random(new Date().getTime());

      List<Short> insts = range(0, INSTRUCTION_SIZE)
          .mapToObj(value -> {
            return (short) random.nextInt(6);
          })
          .collect(toList());
      range(0, insts.size()).forEach(idx -> image.putShort(jobPosition + idx * 2 + 8, insts.get(idx)));
    }
    fileOutputStream.write(image.array(), 0, Kernel.HDD_SIZE);
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  public static void main(String[] args) {
    Formatter formatter = new Formatter("hdd.img");
    try {
      formatter.format();
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }
  }
}
