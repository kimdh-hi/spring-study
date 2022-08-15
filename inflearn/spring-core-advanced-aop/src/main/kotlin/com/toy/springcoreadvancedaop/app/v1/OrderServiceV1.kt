package com.toy.springcoreadvancedaop.app.v1

import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.hellotrace.HelloTraceV1
import org.springframework.stereotype.Service

@Service
class OrderServiceV1(
  private val orderRepositoryV1: OrderRepositoryV1,
  private val helloTrace: HelloTraceV1
) {

  fun orderItem(id: String) {
    var status: TraceStatus? = null
    try {
      status = helloTrace.begin("OrderServiceV1.orderItem")
      orderRepositoryV1.save(id)
    } catch (e: Exception) {
      helloTrace.exception(status!!, e)
      throw e
    }
  }
}