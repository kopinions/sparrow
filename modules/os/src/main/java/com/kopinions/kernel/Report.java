package com.kopinions.kernel;

public interface Report<T> {

  void report(Reporter<T> reporter);
}
