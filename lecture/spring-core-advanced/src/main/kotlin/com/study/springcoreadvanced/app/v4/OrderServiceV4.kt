package com.study.springcoreadvanced.app.v4

import com.study.springcoreadvanced.trace.callback.TraceTemplate
import com.study.springcoreadvanced.trace.logtrace.LogTrace
import org.springframework.stereotype.Service

@Service
class OrderServiceV4(
  private val orderRepository: OrderRepositoryV4,
  private val logTrace: LogTrace
) {

  fun orderItem(id: String) {
    TraceTemplate(logTrace).execute("OrderService.orderItem") {
      orderRepository.save(id)
    }
  }
}