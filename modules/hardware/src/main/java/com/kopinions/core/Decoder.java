package com.kopinions.core;

import com.kopinions.RawInst;

public interface Decoder {

  Command decode(RawInst inst);
}
