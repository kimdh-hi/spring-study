package com.toy.springcoreadvancedaop.app.v1

import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.hellotrace.HelloTraceV1
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV1(
  private val helloTrace: HelloTraceV1
) {

  fun save(id: String) {
    var status: TraceStatus? = null
    try {
      status = helloTrace.begin("OrderRepositoryV1.save")
      if (id == "ex")
        throw RuntimeException("error...")
      Thread.sleep(1000L)
    } catch (e: Exception) {
      helloTrace.exception(status!!, e)
      throw e
    }
  }
}