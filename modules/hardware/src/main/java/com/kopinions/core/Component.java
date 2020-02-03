package com.kopinions.core;

import com.kopinions.Address;

public interface Component {

  void attached(Bus bus);

  short read(Address pa);

  void write(Address address, short data);

  Address.Range range();
}
