package com.kopinions.kernel;

public class IdGenerator implements Generator<Short> {
  short counter = 0;

  @Override
  public synchronized Short generate() {
    return ++counter;
  }
}
