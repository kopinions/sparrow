package com.kopinions.core;

import java.util.List;

public interface Instruction {
  class Operand {

  }

  String operation();

  List<Operand> operands();
}
