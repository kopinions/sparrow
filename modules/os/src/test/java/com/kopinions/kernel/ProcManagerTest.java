package com.kopinions.kernel;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class ProcManagerTest {

  @Test
  void should_able_to_create_proc_from_the_job() {
    ProcManager procManager = new ProcManager(new Selector<Proc>() {
      @Override
      public List<Proc> applied(Queue<Proc> elements) {
        return asList(elements.peek());
      }
    });

    JobManager jobManager = new JobManager();
    ByteBuffer allocate = ByteBuffer.allocate(12);
    int jobOwner = 1000;
    int size = 12;
    allocate.putInt(0, jobOwner);
    allocate.putInt(4, size);
    allocate.putShort(8, (short) 2);
    allocate.putShort(10, (short) 2);
    Job job = jobManager.create(Kernel.HDD_FS_ROOT, 12, allocate.array());

    Proc proc = procManager.create(job);
    assertThat(proc, notNullValue());
  }
}
