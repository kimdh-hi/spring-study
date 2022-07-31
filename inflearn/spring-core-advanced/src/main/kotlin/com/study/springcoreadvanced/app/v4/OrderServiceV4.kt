package com.study.springcoreadvanced.app.v4

import com.study.springcoreadvanced.trace.logtrace.LogTrace
import com.study.springcoreadvanced.trace.template.AbstractTemplate
import org.springframework.stereotype.Service

@Service
class OrderServiceV4(
  private val orderRepository: OrderRepositoryV4,
  private val logTrace: LogTrace
) {

  fun orderItem(id: String) {
    val template = object: AbstractTemplate<Unit>(logTrace) {
      override fun call() {
        orderRepository.save(id)
      }
    }
    template.execute("OrderService.orderItem")
  }
}