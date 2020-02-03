package com.kopinions.core;

public interface Disk extends Component {
  short read(int address);

  void write(int address, short data);
}
