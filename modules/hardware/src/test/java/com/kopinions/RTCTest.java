package com.kopinions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Timer;
import java.util.Date;
import org.junit.jupiter.api.Test;

class RTCTest {

  static class TestableTimer implements Timer {
    @Override
    public void schedule(Task task, Date start, long period) {
      task.run();
    }
  }
  @Test
  public void should_able_to_interrupt_every_one_second() {
    CPU.Interrupter interrupter = mock(CPU.Interrupter.class);
    RTC rtc = new RTC(new TestableTimer(), interrupter);
    rtc.start();
    verify(interrupter).interrupt(eq(Type.RTC));
  }
}
