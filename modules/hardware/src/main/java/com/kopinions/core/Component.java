package com.kopinions.core;

import com.kopinions.Address;

public interface Component {

  void attached(Bus bus);

  Data read(Address pa);

  void write(Address address, Data data);

  Address.Range range();
}
