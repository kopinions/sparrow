package com.kopinions.kernel;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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

  public Job load(String s) {
    Job job = new Job(generator.generate());
    String[] insts = s.split("\n");
    job.instructions = stream(insts).map(Short::valueOf).collect(toList());
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
