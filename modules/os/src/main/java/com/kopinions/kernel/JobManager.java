package com.kopinions.kernel;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobManager implements Report<Map<String, Object>> {

  private final IdGenerator generator;
  Queue<Job> jobs;

  public JobManager() {
    this.generator = new IdGenerator();
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
    job.instructions.remove((size - 8) / 2 - 1);
    job.instructions.add((short) 0xFF);
    jobs.add(job);
    return job;
  }

  public Job id(int id) {
    return null;
  }

  public void unload(int id) {

  }

  @Override
  public void report(Reporter<Map<String, Object>> reporter) {
    HashMap<String, Object> job = new HashMap<>() {{
      Map<String, Object> avail = new HashMap<>();
      avail.put("count", jobs.size());
      put("available", avail);
    }};
    reporter.report(job);
  }

  public Optional<Job> single(Selector<Job> selector) {
    return selector.applied(jobs).stream().map(Optional::ofNullable).findFirst()
        .orElse(Optional.empty());
  }

  public List<Job> select(Selector<Job> selector) {
    return selector.applied(jobs);
  }
}
