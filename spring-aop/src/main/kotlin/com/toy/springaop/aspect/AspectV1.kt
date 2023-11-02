package com.toy.springaop.aspect

import com.toy.springaop.logtrace.LogTrace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory

@Aspect
class AspectV1(
  private val logTrace: LogTrace
) {

  private val log = LoggerFactory.getLogger(javaClass)

  // pointcut signature
  // private, public 모두 가능
  @Pointcut("execution(* com.toy.springaop..*(..))")
  private fun allServicePackage() {}

  @Pointcut("execution(* * ..*Service.*(..))")
  private fun allServiceClass() {}

  @Around("allServicePackage()")
  fun log(joinPoint: ProceedingJoinPoint): Any {
    logTrace.start()

    val result = joinPoint.proceed()

    logTrace.end()

    return result
  }

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