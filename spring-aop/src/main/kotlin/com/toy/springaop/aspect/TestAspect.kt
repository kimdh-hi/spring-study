package com.toy.springaop.aspect

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class TestAspect {

  private val log = LoggerFactory.getLogger(javaClass)

  @Before("@annotation(com.toy.springaop.aspect.TestAnnotation)")
  fun execute() {
    log.info("TestAspect...")
  }
}

