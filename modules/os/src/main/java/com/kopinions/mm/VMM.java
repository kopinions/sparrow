package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.mm.Page.PageDirectory;

public interface VMM {

  PageDirectory pgdir();

  void pgfault(Address va);
}
