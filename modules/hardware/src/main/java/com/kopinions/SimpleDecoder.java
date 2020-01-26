package com.kopinions;

import com.kopinions.core.Command;
import com.kopinions.core.Data;
import com.kopinions.core.Decoder;

public class SimpleDecoder implements Decoder {

  @Override
  public Command decode(Data inst) {
    return new InterruptCommand();
  }
}
