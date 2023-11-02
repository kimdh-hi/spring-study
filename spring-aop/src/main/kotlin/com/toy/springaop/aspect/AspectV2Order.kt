package com.toy.springaop.aspect

import com.toy.springaop.logtrace.LogTrace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

class AspectV2Order {

  @Pointcut("execution(* com.toy.springaop..*(..))")
  private fun allServicePackage() {}

  @Pointcut("execution(* * ..*Service.*(..))")
  private fun allServiceClass() {}

  @Aspect
  @Order(2)
  class LogTraceAspect(
    private val logTrace: LogTrace
  ) {
    @Around("allServicePackage()")
    fun log(joinPoint: ProceedingJoinPoint): Any {
      logTrace.start()

      val result = joinPoint.proceed()

      logTrace.end()

      return result
    }
  }

  @Aspect
  @Order(1)
  class TransactionAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    @Around("allServicePackage() && allServiceClass()")
    fun transaction(joinPoint: ProceedingJoinPoint): Any {
      return try {
        log.info("transaction start...")
        val result = joinPoint.proceed()
        log.info("transaction commit...")
        result
      } catch (e: Exception) {
        log.info("transaction rollback...")
      } finally {
        log.info("transaction close")
      }
    }
  }
}