package com.kopinions;

import com.kopinions.core.Bus;
import com.kopinions.core.Component;
import com.kopinions.core.Data;
import java.util.HashMap;
import java.util.Map;

public class SimpleBus implements Bus {

  private Map<Address.Range, Component> components;
  private int width;

  public SimpleBus(int width) {
    this.width = width;
    this.components = new HashMap<>();
  }

  @Override
  public void attach(Component component) {
    components.put(component.range(), component);
    component.attached(this);
  }

  @Override
  public short read(Address pa) {
    return matched(pa).read(pa);
  }

  private Component matched(Address address) {
    return components.entrySet().stream().filter(c -> c.getKey().contains(address)).findFirst()
          .orElseThrow(() -> new RuntimeException("Can not find components")).getValue();
  }

  @Override
  public void write(Address pa, short data) {
    matched(pa).write(pa, data);
  }
}
