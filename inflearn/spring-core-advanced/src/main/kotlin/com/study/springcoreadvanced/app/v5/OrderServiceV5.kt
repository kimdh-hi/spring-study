package com.study.springcoreadvanced.app.v5

import com.study.springcoreadvanced.trace.callback.TraceTemplate
import com.study.springcoreadvanced.trace.logtrace.LogTrace
import com.study.springcoreadvanced.trace.template.AbstractTemplate
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