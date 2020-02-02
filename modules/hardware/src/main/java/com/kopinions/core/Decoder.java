package com.kopinions.core;

public interface Decoder<T> {
  Instruction decode(T inst);
}
