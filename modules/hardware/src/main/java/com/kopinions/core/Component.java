package com.kopinions.core;

public interface Component {

  void attached(Bus bus);

  Data recv();

  void send(Data data);
}
