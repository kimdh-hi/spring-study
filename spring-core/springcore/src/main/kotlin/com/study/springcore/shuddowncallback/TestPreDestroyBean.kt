package com.study.springcore.shuddowncallback

import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TestPreDestroyBean {

  private val log = LoggerFactory.getLogger(TestPreDestroyBean::class.java)

  @PreDestroy
  fun destroy() {
    log.info("TestDisposableBean destroy.. [@PreDestroy]")
  }
}
