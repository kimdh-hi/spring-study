package com.lecture.inflearndatajpa.base

import com.lecture.inflearndatajpa.domain.event.PostListener
import com.lecture.inflearndatajpa.domain.event.PostPublishedEvent
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun postListener(): PostListener = PostListener()

  @Bean
  fun postSecondListener() = ApplicationListener<PostPublishedEvent> {
    log.info("=======Second PostListener post:{}=======", it.post)
  }
}