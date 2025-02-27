package com.toy.springcoreadvancedaop.app.v4

import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import com.toy.springcoreadvancedaop.trace.template.AbstractTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV4(
  private val orderService: OrderServiceV4,
  private val logTrace: LogTrace
) {

  @GetMapping("/v4/request/{id}")
  fun request(@PathVariable id: String): String {
    val template = object: AbstractTemplate<String>(logTrace) {
      override fun call(): String {
          orderService.orderItem(id)
          return "ok"
      }
    }
    return template.execute("OrderController.request")
  }
}