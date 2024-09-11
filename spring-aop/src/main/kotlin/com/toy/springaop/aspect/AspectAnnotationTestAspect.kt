package com.toy.springaop.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class AspectAnnotationTestAspect(
  private val testAnnotationDelegate: TestAnnotationDelegate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Around("execution(* com.toy.springaop.controller.*.*(..))")
  fun testAspect(joinPoint: ProceedingJoinPoint): Any? {
    log.info("AspectAnnotationTestAspect.testAspect")
    return testAnnotationDelegate.execute(joinPoint)
  }
}

@Component
class TestAnnotationDelegate {

  @TestAnnotation
  fun execute(joinPoint: ProceedingJoinPoint): Any? {
    return joinPoint.proceed()
  }

}