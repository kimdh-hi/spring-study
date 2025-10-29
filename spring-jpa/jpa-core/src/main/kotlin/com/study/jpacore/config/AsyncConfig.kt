package com.study.jpacore.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@EnableAsync
@Configuration
class AsyncConfig {

  //threads-virtual.enabled=true
  @Bean
  fun asyncTaskExecutor(): Executor {
    val taskExecutorAdapter = TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor())
    return taskExecutorAdapter
  }

}
