package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Component;

public class SimpleBus implements Bus {
  public SimpleBus(int width) {

  }

  @Override
  public void attach(Component component) {
    component.attached(this);
  }
}
