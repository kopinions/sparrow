package com.kopinions;

import com.kopinions.core.CPU;
import com.kopinions.core.Instruction;
import java.util.ArrayList;
import java.util.List;

public class MoveInstruction implements Instruction {

  private final String register;
  private final short value;

  public MoveInstruction(String register, short value) {
    this.register = register;
    this.value = value;
  }

  @Override
  public Operation operation() {
    return Operation.MOVE;
  }

  @Override
  public List<Operand> operands() {
    return new ArrayList<>();
  }

  @Override
  public int cycle() {
    return 1;
  }

  @Override
  public void applied(CPU cpu) {
    SISD cpu1 = (SISD) cpu;
    try {
      cpu1.getClass().getDeclaredField(register).set(cpu1, value);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
}
