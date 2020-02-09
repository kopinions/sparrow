package com.kopinions.mm;

import static com.kopinions.mm.Page.Status.USING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.kopinions.DRAM;
import com.kopinions.kernel.Kernel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageBasePMMTest {

  private PMM pmm;
  private static final int TOTAL_PAGES = 32;

  @BeforeEach
  void setUp() {
    DRAM dram = new DRAM(Kernel.MEM_SIZE);
    pmm = new PageBasePMM(dram, 16 * 1024, 512);
  }

  @Test
  void should_able_to_alloc_one_page() {
    Page alloced = pmm.alloc();
    assertThat(alloced, notNullValue());
    assertThat(alloced.is(USING), is(true));
  }

  @Test
  void should_able_to_alloc_some_pages() {
    List<Page> alloced = pmm.alloc(10);
    assertThat(alloced, notNullValue());
    assertThat(alloced.size(), is(10));
    alloced.forEach(p -> {
      assertThat(p.is(USING), is(true));
    });
  }

  @Test
  void should_able_to_exception_when_alloc_page_with_no_memory_left() {
    pmm.alloc(TOTAL_PAGES);

    assertThrows(RuntimeException.class, pmm::alloc);
  }

  @Test
  void should_exception_when_alloc_list_with_no_memory_left() {
    pmm.alloc(20);

    assertThrows(RuntimeException.class, () -> pmm.alloc(20));
  }

  @Test
  void should_able_to_free_page() {
    Page alloc = pmm.alloc();
    pmm.free(alloc);

    assertThat(pmm.available(), is(TOTAL_PAGES));
  }

  @Test
  void should_able_to_free_pages() {
    List<Page> pages = pmm.alloc(10);
    pmm.free(pages);

    assertThat(pmm.available(), is(TOTAL_PAGES));
  }

  @Test
  void should_get_physical_address_of_page() {
    Page alloc = pmm.alloc();
    int pa = alloc.pa();
    assertThat(pa, is(0 + Kernel.MEM_USERSPACE_SIZE));
  }

  @Test
  void should_able_to_get_page_from_linear_address() {
    Page firstPage = pmm.from((short) 0x0100);
    assertThat(firstPage.index, is(0));

    Page secondPage = pmm.from((short) 0x0200);
    assertThat(secondPage.index, is(1));
  }
}
