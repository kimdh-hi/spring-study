package com.toy.springaop.proxy

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

@Aspect
class ProxyDIAspect {

  private val log = LoggerFactory.getLogger(javaClass)

  @Before("execution(* com.toy.springaop..*.*(..))")
  fun log(joinPoint: JoinPoint) {
    log.info("ProxyDIAspect {}", joinPoint.signature)
  }
}