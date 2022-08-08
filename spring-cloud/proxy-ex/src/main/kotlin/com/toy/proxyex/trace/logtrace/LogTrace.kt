package com.toy.proxyex.trace.logtrace

import com.toy.proxyex.trace.TraceStatus

interface LogTrace {

  fun begin(message: String): TraceStatus

  fun end(status: TraceStatus): Unit

  fun exception(status: TraceStatus, e: Exception)
}