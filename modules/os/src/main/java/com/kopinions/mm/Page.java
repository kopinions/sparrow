package com.kopinions.mm;

import com.kopinions.kernel.Kernel;
import java.util.Objects;

public class Page {

  enum Status {
    USING,
    FREE
  }

  int index;
  private PMM pmm;
  int size;
  Status status;

  public Page(int index, PMM pmm) {
    this.index = index;
    this.pmm = pmm;
    status = Status.FREE;
  }

  public boolean is(Status expected) {
    return status == expected;
  }

  public int size() {
    return size;
  }

  public int pa() {
    int ppn = this.index;
    return (ppn << Kernel.PGSHIFT) + pmm.start();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Page page = (Page) o;
    return index == page.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index);
  }
}
