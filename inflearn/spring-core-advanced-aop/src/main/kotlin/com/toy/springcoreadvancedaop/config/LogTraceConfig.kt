package com.toy.springcoreadvancedaop.config

import com.toy.springcoreadvancedaop.trace.logtrace.ThreadLocalLogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfig {

  @Bean
  fun logTrace() = ThreadLocalLogTrace()
}