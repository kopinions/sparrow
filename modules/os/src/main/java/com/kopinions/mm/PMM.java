package com.kopinions.mm;

import com.kopinions.Address;
import com.kopinions.mm.Page.PageDirectory;
import com.kopinions.mm.Page.PageDirectory.PageDirectoryEntry;
import com.kopinions.mm.Page.PageTable.PageTableEntry;
import java.util.List;

public interface PMM {

  List<Page> alloc(int size);

  Page alloc();

  void free(List<Page> pages);

  void free(Page page);

  int available();

  int ppn(Page page);

  Page from(short la);

  int start();

  PageTableEntry pde(Address pgdir, short la);

  PageTableEntry pte(PageDirectory pgdir, Address addr);
}
