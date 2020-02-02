package com.kopinions.core;

import com.kopinions.Address;

public interface Bus {
  void attach(Component component);

  short read(Address pa);

  void write(Address pa, short data);
}
