package com.kopinions.kernel;

public class IdGenerator implements Generator<Integer> {
  int counter = 0;

  @Override
  public synchronized Integer generate() {
    return ++counter;
  }
}
