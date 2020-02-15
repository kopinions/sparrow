package com.kopinions.core;

import java.util.List;

public interface Instruction {
  class Operand {

  }

  Operation operation();

  List<Operand> operands();

  enum Operation {
  SYSCALL, USER_MODE, IO, MOVE, RET
  }

  int cycle();

  void applied(CPU cpu);
}
