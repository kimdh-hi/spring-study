package com.toy.springcore.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class PerfLogAop {
  private val log = LoggerFactory.getLogger(javaClass)

//  @Around("execution(* com.toy.springcore..*.EventService.*(..))")
  @Around("@annotation(PerfLog)")
  fun execute(pjp: ProceedingJoinPoint): Any? {
    val startTime = System.currentTimeMillis()
    val result = pjp.proceed()
    log.info("execute time: {}", System.currentTimeMillis() - startTime)
    return result
  }

  /*
  특정 bean 의 public method 에 aop 적용
   */
  @Before("bean(eventServiceImpl)")
  fun beforeAop() {
    log.info("before... test...")
  }
}