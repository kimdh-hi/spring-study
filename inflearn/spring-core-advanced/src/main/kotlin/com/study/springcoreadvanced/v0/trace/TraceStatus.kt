package com.study.springcoreadvanced.v0.trace

data class TraceStatus (
  val traceId: TraceId,
  val startTimeMs: Long,
  val message: String
)