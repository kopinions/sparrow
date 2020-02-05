package com.kopinions.kernel;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class JobManagerTest {

  @Test
  public void should_able_to_load_jobs() {
    JobManager jobManager = new JobManager();
    ByteBuffer allocate = ByteBuffer.allocate(12);
    int jobOwner = 1000;
    int size = 12;
    allocate.putInt(0, jobOwner);
    allocate.putInt(4, size);
    allocate.putShort(8, (short) 2);
    allocate.putShort(10, (short) 2);
    Job job = jobManager.create(Kernel.HDD_FS_ROOT, 12, allocate.array());
    assertEquals(job.id, 1);
    assertEquals(job.instructions().size(), 2);
    job.instructions().forEach(i -> assertEquals(i, Short.valueOf("2")));

    assertEquals(jobManager.select(ArrayList::new).size(), 1);
  }
}
