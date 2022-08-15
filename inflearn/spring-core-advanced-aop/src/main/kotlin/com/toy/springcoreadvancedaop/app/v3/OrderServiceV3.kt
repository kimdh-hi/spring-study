package com.toy.springcoreadvancedaop.app.v3

import com.toy.springcoreadvancedaop.trace.TraceId
import com.toy.springcoreadvancedaop.trace.TraceStatus
import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import org.springframework.stereotype.Service

@Service
class OrderServiceV3(
  private val orderRepository: OrderRepositoryV3,
  private val logTrace: LogTrace
) {

  fun orderItem(id: String, traceId: TraceId) {
    var status: TraceStatus? = null
    try {
      status = logTrace.begin("OrderService.orderItem")
      orderRepository.save(id, status.traceId)
    } catch (e: Exception) {
      logTrace.exception(status!!, e)
      throw e
    }
  }
}