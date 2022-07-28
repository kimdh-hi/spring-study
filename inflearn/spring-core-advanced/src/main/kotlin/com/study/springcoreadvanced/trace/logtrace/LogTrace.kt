package com.study.springcoreadvanced.trace.logtrace

import com.study.springcoreadvanced.trace.TraceStatus

interface LogTrace {

  fun begin(message: String): TraceStatus

  fun end(status: TraceStatus): Unit

  fun exception(status: TraceStatus, e: Exception)
}