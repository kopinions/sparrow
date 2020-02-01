package com.kopinions.kernel;

public class StringReporter implements Reporter<String> {
  @Override
  public void report(String message) {
    System.out.println(message);
  }
}
