package com.study.springcoreadvanced.app.v5

import com.study.springcoreadvanced.trace.callback.TraceTemplate
import com.study.springcoreadvanced.trace.logtrace.LogTrace
import com.study.springcoreadvanced.trace.template.AbstractTemplate
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