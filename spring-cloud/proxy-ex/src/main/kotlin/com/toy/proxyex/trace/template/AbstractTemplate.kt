package com.toy.proxyex.trace.template

import com.toy.proxyex.trace.TraceStatus
import com.toy.proxyex.trace.logtrace.LogTrace

abstract class AbstractTemplate<T>(
  private val logTrace: LogTrace
) {

  fun execute(message: String): T {
    var status: TraceStatus? = null
    try {
      status = logTrace.begin(message)

      val result = call()

      logTrace.end(status)
      return result
    } catch (e: Exception) {
      logTrace.exception(status!!, e)
     throw e
    }
  }

  protected abstract fun call(): T
}