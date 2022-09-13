package com.study.springcoreadvanced.config

import com.study.springcoreadvanced.trace.logtrace.FieldSyncLogTrace
import com.study.springcoreadvanced.trace.logtrace.ThreadLocalLogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfig {

  @Bean
  fun logTrace() = ThreadLocalLogTrace()
}