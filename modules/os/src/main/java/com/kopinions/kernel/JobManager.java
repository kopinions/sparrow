package com.kopinions.kernel;

import java.util.HashMap;
import java.util.Map;

public class JobManager {

  public Job load() {
    return null;
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

  public void select(Selector<Job> selector) {

  }
}
