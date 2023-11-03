package com.toy.springaop.aspect

import com.toy.springaop.logtrace.LogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AopConfig {

//  @Bean
//  fun logTraceAspect(logTrace: LogTrace) = AspectV2Order.LogTraceAspect(logTrace)
//
//  @Bean
//  fun transactionAspect() = AspectV2Order.TransactionAspect()

  @Bean
  fun aspect(logTrace: LogTrace) = AspectV1(logTrace)
}