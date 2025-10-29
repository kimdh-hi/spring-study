package com.study.jpacore.aop

import com.study.jpacore.common.QueryCounter
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnProperty(
  name = ["custom.use-query-counter"],
  havingValue = "true",
  matchIfMissing = false
)
class QueryCounterAop {

  private val log = LoggerFactory.getLogger(this.javaClass)

  @Around("execution(* com..*.*Service.*(..))")
  fun logQueryCount(joinPoint: ProceedingJoinPoint): Any? {
    QueryCounter.clear()
    val result = joinPoint.proceed()
    if (QueryCounter.needLogging()) {
      val methodName = "${joinPoint.signature.declaringType.simpleName}.${joinPoint.signature.name}"
      log.info("[{}] Query count: {}", methodName, QueryCounter.get())
    }

    return result
  }
}
