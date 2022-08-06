package com.study.springcoreadvanced.app.v5

import com.study.springcoreadvanced.trace.callback.TraceTemplate
import com.study.springcoreadvanced.trace.logtrace.LogTrace
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV5(
  private val orderService: OrderServiceV5,
  private val logTrace: LogTrace
) {

  var template: TraceTemplate = TraceTemplate(logTrace)

  @GetMapping("/v5/request/{id}")
  fun request(@PathVariable id: String): String {
    return template.execute("OrderController.request") {
      orderService.orderItem(id)
      "ok"
    }
  }
}