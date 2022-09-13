package com.toy.springcoreadvancedaop.app.v1

import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.hellotrace.HelloTraceV1
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV1(
  private val orderServiceV1: OrderServiceV1,
  private val helloTrace: HelloTraceV1
) {

  @GetMapping("/v1/request/{id}")
  fun request(@PathVariable id: String): String {
    var status: TraceStatus? = null
    try {
      status = helloTrace.begin("OrderControllerV1.request")
      orderServiceV1.orderItem(id)
      helloTrace.end(status)
      return "ok"
    } catch (e: Exception) {
      helloTrace.exception(status!!, e)
      throw e
    }
  }
}