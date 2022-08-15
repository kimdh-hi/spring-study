package com.toy.springcoreadvancedaop.app.v5

import com.toy.springcoreadvancedaop.trace.callback.TraceTemplate
import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import org.springframework.stereotype.Service

@Service
class OrderServiceV5(
  private val orderRepository: OrderRepositoryV5,
  private val logTrace: LogTrace
) {

  var template: TraceTemplate = TraceTemplate(logTrace)

  fun orderItem(id: String) {
    template.execute("OrderService.orderItem") {
      orderRepository.save(id)
    }
  }
}