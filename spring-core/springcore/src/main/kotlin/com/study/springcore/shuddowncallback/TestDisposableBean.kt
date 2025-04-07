package com.study.springcore.shuddowncallback

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Component

@Component
class TestDisposableBean : DisposableBean {

  private val log = LoggerFactory.getLogger(TestDisposableBean::class.java)

  override fun destroy() {
    log.info("TestDisposableBean destroy.. [DisposableBean]")
  }
}
