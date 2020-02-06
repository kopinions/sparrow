package com.kopinions.mm;

import static com.kopinions.mm.Page.Status.USING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageBasePMMTest {

  private PageBasePMM pageBasePMM;
  private static final int TOTAL_PAGES = 32;

  @BeforeEach
  void setUp() {
    pageBasePMM = new PageBasePMM(16 * 1024, 512);
  }

  @Test
  void should_able_to_alloc_one_page() {
    Page alloced = pageBasePMM.alloc();
    assertThat(alloced, notNullValue());
    assertThat(alloced.is(USING), is(true));
  }

  @Test
  void should_able_to_alloc_some_pages() {
    List<Page> alloced = pageBasePMM.alloc(10);
    assertThat(alloced, notNullValue());
    assertThat(alloced.size(), is(10));
    alloced.forEach(p -> {
      assertThat(p.is(USING), is(true));
    });
  }

  @Test
  void should_able_to_exception_when_alloc_page_with_no_memory_left() {
    pageBasePMM.alloc(TOTAL_PAGES);

    assertThrows(RuntimeException.class, pageBasePMM::alloc);
  }

  @Test
  void should_exception_when_alloc_list_with_no_memory_left() {
    pageBasePMM.alloc(20);

    assertThrows(RuntimeException.class, () -> pageBasePMM.alloc(20));
  }

  @Test
  void should_able_to_free_page() {
    Page alloc = pageBasePMM.alloc();
    pageBasePMM.free(alloc);

    assertThat(pageBasePMM.available(), is(TOTAL_PAGES));
  }

  @Test
  void should_able_to_free_pages() {
    List<Page> pages = pageBasePMM.alloc(10);
    pageBasePMM.free(pages);

    assertThat(pageBasePMM.available(), is(TOTAL_PAGES));
  }
}
