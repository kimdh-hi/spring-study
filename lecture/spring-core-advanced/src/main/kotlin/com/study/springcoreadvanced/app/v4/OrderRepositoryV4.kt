package com.study.springcoreadvanced.app.v4

import com.study.springcoreadvanced.trace.TraceId
import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.logtrace.LogTrace
import com.study.springcoreadvanced.trace.template.AbstractTemplate
import org.springframework.stereotype.Repository
import kotlin.math.log

@Repository
class OrderRepositoryV4(
  private val logTrace: LogTrace
) {

  fun save(id: String) {
    val template = object: AbstractTemplate<Unit>(logTrace) {
      override fun call() {
        if (id == "ex")
          throw RuntimeException("error...")
        Thread.sleep(1000L)
      }
    }
    template.execute("OrderRepository.save")
  }
}