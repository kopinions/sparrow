package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Component;

public class SimpleBus implements Bus {

  private int width;

  public SimpleBus(int width) {
    this.width = width;
  }

  @Override
  public void attach(Component component) {
    component.attached(this);
  }
}
