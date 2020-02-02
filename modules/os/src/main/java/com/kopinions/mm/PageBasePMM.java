package com.kopinions.mm;

import java.util.List;

public class PageBasePMM implements PMM {

  @Override
  public List<Page> alloc(int size) {
    return null;
  }

  @Override
  public Page alloc() {
    return null;
  }

  @Override
  public void free(List<Page> pages) {

  }

  @Override
  public void free(Page page) {

  }

  @Override
  public int available() {
    return 0;
  }
}
