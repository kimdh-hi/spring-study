package com.toy.springaop.aspect

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class AspectAnnotationTestAspect {

  private val log = LoggerFactory.getLogger(javaClass)

  @Before("execution(* com.toy.springaop.controller.*.*(..))")
  @TestAnnotation
  fun testAspect() {
    log.info("AspectAnnotationTestAspect.testAspect")
  }
}