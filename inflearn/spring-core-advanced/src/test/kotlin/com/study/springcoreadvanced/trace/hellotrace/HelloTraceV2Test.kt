package com.study.springcoreadvanced.trace.hellotrace

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HelloTraceV2Test {
  @Test
  fun begin_end() {
    val trace = HelloTraceV2()
    val trace1Status = trace.begin("hello")
    val trace2Status = trace.beginSync(trace1Status.traceId, "hello222")
    trace.end(trace1Status)
    trace.end(trace2Status)
  }
  @Test
  fun begin_exception() {
    val trace = HelloTraceV2()
    val trace1Status = trace.begin("hello")
    val trace2Status = trace.beginSync(trace1Status.traceId, "hello222")
    trace.exception(trace2Status, IllegalStateException())
    trace.exception(trace1Status, IllegalStateException())
  }
}