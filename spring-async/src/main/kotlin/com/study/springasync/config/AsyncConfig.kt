package com.study.springasync.config

import com.study.springasync.service.UserIdHolder
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@Configuration
class AsyncConfig {

  @Bean
  fun threadPoolTaskExecutorCustomizer() = ThreadPoolTaskExecutorCustomizer {
    it.setTaskDecorator(ThreadLocalCopyDecorator())
  }
}

class ThreadLocalCopyDecorator : TaskDecorator {
  override fun decorate(runnable: Runnable): Runnable {
    val userId = UserIdHolder.get()
    return Runnable {
      try {
        userId?.let { UserIdHolder.set(it) }
        runnable.run()
      } finally {
        UserIdHolder.clear()
      }
    }
  }
}
