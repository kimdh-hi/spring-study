package com.study.springcoreadvanced.app.v2

import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV1
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV2
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV2(
  private val orderServiceV2: OrderServiceV2,
  private val helloTrace: HelloTraceV2
) {

  @GetMapping("/v2/request/{id}")
  fun request(@PathVariable id: String): String {
    var status: TraceStatus? = null
    try {
      status = helloTrace.begin("OrderControllerV2.request")
      orderServiceV2.orderItem(id, status.traceId)
      helloTrace.end(status)
      return "ok"
    } catch (e: Exception) {
      helloTrace.exception(status!!, e)
      throw e
    }
  }
}