package com.kopinions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.kopinions.Interrupter.Type;
import org.junit.jupiter.api.Test;

class RTCTest {

  @Test
  public void should_able_to_interrupt_every_one_second() {
    Timer timer = mock(Timer.class);
    Interrupter interrupter = mock(Interrupter.class);
    RTC rtc = new RTC(timer, interrupter);
    rtc.start();
    verify(timer).schedule(any(), any(), eq(1000L));
    verify(interrupter).interrupt(eq(Type.RTC));
  }
}
