package com.study.springcoreadvanced.trace.logtrace

import org.junit.jupiter.api.Test

internal class ThreadLocalLogTraceTest {

  @Test
  fun fieldSyncLogTrace() {
    val trace = ThreadLocalLogTrace()

    val status1 = trace.begin("hello1")
    val status2 = trace.begin("hello2")
    val status3 = trace.begin("hello3")
    trace.end(status3)
    trace.end(status2)
    trace.end(status1)
  }

  @Test
  fun fieldSyncLogTraceException() {
    val trace = ThreadLocalLogTrace()

    val status1 = trace.begin("hello1")
    val status2 = trace.begin("hello2")
    val status3 = trace.begin("hello3")
    trace.exception(status3, RuntimeException("error..."))
    trace.end(status2)
    trace.end(status1)
  }
}