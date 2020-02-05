package com.kopinions.core;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public interface Disk extends Component {
  short read(int address);

  void write(int address, short data);
}
