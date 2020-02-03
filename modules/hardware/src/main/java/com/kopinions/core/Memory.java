package com.kopinions.core;

import com.kopinions.Address;

public interface Memory extends Component {
  short read(Address address);

  void memset(int from, int to, byte value);

  int size();
}
