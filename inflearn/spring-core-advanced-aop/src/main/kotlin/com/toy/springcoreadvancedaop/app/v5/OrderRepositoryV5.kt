package com.toy.springcoreadvancedaop.app.v5

import com.toy.springcoreadvancedaop.trace.callback.TraceTemplate
import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV5(
  private val logTrace: LogTrace
) {

  var template: TraceTemplate = TraceTemplate(logTrace)

  fun save(id: String) {
    template.execute("OrderRepository.save") {
      if (id == "ex")
        throw RuntimeException("error...")
      Thread.sleep(1000L)
    }
  }
}