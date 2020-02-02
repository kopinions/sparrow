package com.kopinions.kernel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class JobManagerTest {

  @Test
  public void should_able_to_load_jobs() {
    JobManager jobManager = new JobManager();
    Job job = jobManager.load("2\n"
        + "2");
    assertEquals(job.id, 1);
    assertEquals(job.instructions().size(), 2);
    job.instructions().forEach(i -> assertEquals(i, Short.valueOf("2")));

    assertEquals(jobManager.select(ArrayList::new).size(), 1);
  }


  @Test
  public void should_count_used_pages() {
    JobManager jobManager = new JobManager();
    Job job = jobManager.load("2\n"
        + "2");
  }
}
