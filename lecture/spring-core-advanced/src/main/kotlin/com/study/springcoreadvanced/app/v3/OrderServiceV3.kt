package com.study.springcoreadvanced.app.v3

import com.study.springcoreadvanced.trace.TraceId
import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV2
import com.study.springcoreadvanced.trace.logtrace.LogTrace
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