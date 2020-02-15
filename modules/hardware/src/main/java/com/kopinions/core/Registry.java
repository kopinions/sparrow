package com.kopinions.core;

public interface Registry {

  enum Name {
    CR1, CR2, CR3, EIP, EBP, EDI
  }

  short get(Name name);

  void set(Name name, short value);

  void backup();

  void restore();
}
