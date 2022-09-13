package com.toy.springcoreadvancedaop.app.v3

import com.toy.springcoreadvancedaop.trace.TraceId
import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV3(
  private val logTrace: LogTrace
) {

  fun save(id: String, traceId: TraceId) {
    var status: TraceStatus? = null
    try {
      status = logTrace.begin("OrderRepository.save")
      if (id == "ex")
        throw RuntimeException("error...")
      Thread.sleep(1000L)
    } catch (e: Exception) {
      logTrace.exception(status!!, e)
      throw e
    }
  }
}