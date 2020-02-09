package com.kopinions.kernel;

import com.kopinions.Address;
import com.kopinions.mm.Page;
import com.kopinions.mm.VMM;

public interface SwapManager {
  void out(VMM vmm, int pages);

  Page in(VMM vmm, Address addr);
}
