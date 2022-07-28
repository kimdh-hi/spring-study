package com.study.springcoreadvanced.v0.trace.hellotrace

import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV1
import org.junit.jupiter.api.Test

internal class HelloTraceV1Test {
  @Test
  fun begin_end() {
    val trace = HelloTraceV1()
    val status = trace.begin("hello")
    trace.end(status)
  }
  @Test
  fun begin_exception() {
    val trace = HelloTraceV1()
    val status = trace.begin ("hello");
    trace.exception(status, IllegalStateException())
  }
}