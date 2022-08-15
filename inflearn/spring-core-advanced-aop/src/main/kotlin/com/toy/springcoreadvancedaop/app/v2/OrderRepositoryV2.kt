package com.study.springcoreadvanced.app.v2

import com.toy.springcoreadvancedaop.trace.TraceId
import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.hellotrace.HelloTraceV2
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV2(
  private val helloTrace: HelloTraceV2
) {

  fun save(id: String, traceId: TraceId) {
    var status: TraceStatus? = null
    try {
      status = helloTrace.beginSync(traceId, "OrderRepositoryV2.save")
      if (id == "ex")
        throw RuntimeException("error...")
      Thread.sleep(1000L)
    } catch (e: Exception) {
      helloTrace.exception(status!!, e)
      throw e
    }
  }
}