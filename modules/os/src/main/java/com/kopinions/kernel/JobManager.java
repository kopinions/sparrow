package com.kopinions.kernel;

public class JobManager {

  public Job load() {
    return null;
  }

  public Job id(int id) {
    return null;
  }

  public void unload(int id) {

  }

  public void report(Reporter<String> reporter) {
    reporter.report("Reported by the job manager");
  }

  public void select(Selector<Job> selector) {

  }
}
