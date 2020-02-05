package com.kopinions.kernel;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobManager {

  static Generator<Integer> generator = new IdGenerator();
  Queue<Job> jobs;

  public JobManager() {
    this.jobs = new LinkedBlockingQueue<>();
  }

  public Job create(int location, int size, byte[] jobdata) {
    Job job = new Job(generator.generate());
    job.location = location;
    job.size = size;
    ByteBuffer jobBufer = ByteBuffer.wrap(jobdata);
    job.instructions = range(0, (size - 8) / 2)
        .mapToObj(i -> jobBufer.getShort(8 + i * 2))
        .collect(toList());
    jobs.add(job);
    return job;
  }

  public Job id(int id) {
    return null;
  }

  public void unload(int id) {

  }


  public void report(Reporter<Map<String, Object>> reporter) {
    reporter.report(new HashMap<String, Object>() {{
      put("test", "test");
    }});
  }

  public Job single(Selector<Job> selector) {
    return selector.applied(jobs).stream().findFirst().orElse(null);
  }

  public List<Job> select(Selector<Job> selector) {
    return selector.applied(jobs);
  }
}
