package com.study.springcoreadvanced.app.v3

import com.study.springcoreadvanced.trace.TraceId
import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV2
import com.study.springcoreadvanced.trace.logtrace.LogTrace
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