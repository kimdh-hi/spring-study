package com.study.springcoreadvanced.app.v2

import com.study.springcoreadvanced.trace.TraceId
import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV1
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV2
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