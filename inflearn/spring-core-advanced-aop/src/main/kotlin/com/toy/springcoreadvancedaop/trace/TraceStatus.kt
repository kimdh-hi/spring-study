package com.toy.springcoreadvancedaop.trace

import com.toy.springcoreadvancedaop.trace.TraceId

data class TraceStatus (
  val traceId: TraceId,
  val startTimeMs: Long,
  val message: String
)