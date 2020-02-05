package com.kopinions.kernel;

import java.util.List;
import java.util.Map;

public class Job {

  String owner;
  int id;
  int process;
  int location;
  int loaded;
  int startedAt;
  short addr;
  int numOfInstructions;
  int numOfPage;
  List<Short> instructions;

  public Job(int id) {
    this.id = id;
    owner = "user";
  }

  public List<Short> instructions() {
    return instructions;
  }
}
