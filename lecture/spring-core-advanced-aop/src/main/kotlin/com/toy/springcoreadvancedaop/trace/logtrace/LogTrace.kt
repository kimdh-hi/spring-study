package com.toy.springcoreadvancedaop.trace.logtrace

import com.toy.springcoreadvancedaop.trace.TraceStatus

interface LogTrace {

  fun begin(message: String): TraceStatus

  fun end(status: TraceStatus): Unit

  fun exception(status: TraceStatus, e: Exception)
}