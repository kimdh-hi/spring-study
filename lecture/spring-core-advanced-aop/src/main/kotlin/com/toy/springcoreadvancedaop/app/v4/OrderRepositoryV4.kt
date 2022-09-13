package com.toy.springcoreadvancedaop.app.v4

import com.toy.springcoreadvancedaop.trace.logtrace.LogTrace
import com.toy.springcoreadvancedaop.trace.template.AbstractTemplate
import org.springframework.stereotype.Repository

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