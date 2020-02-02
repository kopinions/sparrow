package com.kopinions.mm;

import java.util.List;

public interface PMM {

  List<Page> alloc(int size);

  Page alloc();

  void free(List<Page> pages);

  void free(Page page);

  int available();
}
