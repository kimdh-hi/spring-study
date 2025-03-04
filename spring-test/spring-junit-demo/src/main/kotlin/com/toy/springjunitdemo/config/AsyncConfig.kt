package com.toy.springjunitdemo.config

import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {

  companion object {
    const val THREAD_NAME_PREFIX = "async-executor-"
  }

  override fun getAsyncExecutor(): Executor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = 25
    executor.maxPoolSize = 100
    executor.queueCapacity = 50
    executor.setThreadNamePrefix(THREAD_NAME_PREFIX)
    executor.initialize()
    return executor
  }

  override fun getAsyncUncaughtExceptionHandler() = SimpleAsyncUncaughtExceptionHandler()

}