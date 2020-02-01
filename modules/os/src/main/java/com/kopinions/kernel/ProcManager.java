package com.kopinions.kernel;

import java.util.ArrayList;
import java.util.List;

public class ProcManager {

  List<Proc> processes;

  public ProcManager() {
    this.processes = new ArrayList<>();
  }

  Proc create() {
    return null;
  }

  void kill(Proc proc) {

  }

  Proc id(int id) {
    return null;

  }

  void report(Reporter<String> reporter) {
    reporter.report("");
  }

  Proc select(Selector<Proc> selector) {
    return selector.applied(null);
  }
}
