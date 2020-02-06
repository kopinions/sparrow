package com.kopinions.mm;

public class Page {

    public boolean is(Status expected) {
        return status == expected;
    }

    enum Status {
    USING,
    FREE
  }

  int zone_num;
  int size;
  Status status;

  public Page(int zone_num) {
    this.zone_num = zone_num;
    status = Status.FREE;
  }

  public int size() {
    return size;
  }
}
