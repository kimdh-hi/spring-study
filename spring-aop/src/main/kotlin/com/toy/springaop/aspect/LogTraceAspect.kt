package com.toy.springaop.aspect

import com.toy.springaop.logtrace.LogTrace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class LogTraceAspect(
  private val logTrace: LogTrace
) {

  // pointcut signature
  // private, public 모두 가능
  @Pointcut("execution(* com.toy.springaop.service..*(..))")
  private fun pointcut() {}

  @Around("pointcut()")
  fun execute(joinPoint: ProceedingJoinPoint): Any {
    logTrace.start()

    val result = joinPoint.proceed()

    logTrace.end()

    return result
  }
}