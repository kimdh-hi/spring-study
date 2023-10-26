package com.toy.springaop.aspect

import com.toy.springaop.logtrace.LogTrace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class LogTraceAspect(
  private val logTrace: LogTrace
) {

  @Around("execution(* com.toy.springaop.service..*(..))")
  fun execute(joinPoint: ProceedingJoinPoint): Any {
    logTrace.start()

    val result = joinPoint.proceed()

    logTrace.end()

    return result
  }
}