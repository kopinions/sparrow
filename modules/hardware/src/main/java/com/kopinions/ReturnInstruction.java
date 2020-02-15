package com.kopinions;

import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Instruction;
import java.util.List;

public class ReturnInstruction implements Instruction {

  @Override
  public Operation operation() {
    return Operation.RET;
  }

  @Override
  public List<Operand> operands() {
    return null;
  }

  @Override
  public int cycle() {
    return 1;
  }

  @Override
  public void applied(CPU cpu) {
    cpu.interrupter().interrupt(Type.SWITCH);
    cpu.registry().restore();
  }
}
