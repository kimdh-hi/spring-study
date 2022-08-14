package com.toy.springcoreadvancedaop.trace.callback

import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace

class TraceTemplate(
  private val logTrace: LogTrace
) {

  fun <T> execute(message: String, callback: TraceCallback<T>): T {
    var status: TraceStatus? = null
    try {
      status = logTrace.begin(message)

      val result = callback.call()

      logTrace.end(status)
      return result
    } catch (e: Exception) {
      logTrace.exception(status!!, e)
      throw e
    }
  }
}