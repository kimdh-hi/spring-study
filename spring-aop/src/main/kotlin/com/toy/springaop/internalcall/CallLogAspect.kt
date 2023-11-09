package com.toy.springaop.internalcall

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

@Aspect
class CallLogAspect {

  private val log = LoggerFactory.getLogger(javaClass)

  @Before("execution(* com.toy.springaop.internalcall..*.*(..))")
  fun doLog(joinPoint: JoinPoint) {
    log.info("doLog={}", joinPoint.signature)
  }
}