package com.study.springcore.shuddowncallback

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestDestroyMethodBeanConfig {

  @Bean(destroyMethod = "destroy")
  fun bean(): TestDestroyMethodBean {
    return TestDestroyMethodBean()
  }
}

class TestDestroyMethodBean {

  private val log = LoggerFactory.getLogger(TestDestroyMethodBean::class.java)

  fun destroy() {
    log.info("TestDisposableBean destroy.. [destroyMethod]")
  }
}
