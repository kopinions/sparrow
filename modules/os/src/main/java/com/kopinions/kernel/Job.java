package com.kopinions.kernel;

import java.util.List;
import java.util.Map;

public class Job {

  int priority;
  String owner;
  int id;
  int process;
  int location;
  int size;
  int loaded;
  int startedAt;
  short addr;
  int numOfInstructions;
  int numOfPage;
  List<Short> instructions;

  public Job(int id) {
    this.id = id;
    owner = "user";
    priority = 1;
  }

  public List<Short> instructions() {
    return instructions;
  }

}
