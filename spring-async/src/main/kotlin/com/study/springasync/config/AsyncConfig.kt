package com.study.springasync.config

import com.study.springasync.service.UserIdHolder
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable

@EnableAsync
@Configuration
class AsyncConfig {

  @Bean
  fun threadPoolTaskExecutorCustomizer() = ThreadPoolTaskExecutorCustomizer { customizer ->
    customizer.setTaskDecorator(CustomTaskDecorator())
  }
}

class CustomTaskDecorator : TaskDecorator {
  override fun decorate(runnable: Runnable): Runnable {
    val securityRunnable = DelegatingSecurityContextRunnable(runnable)
    val userId = UserIdHolder.get()
    return Runnable {
      try {
        userId?.let { UserIdHolder.set(it) }
        securityRunnable.run()
      } finally {
        UserIdHolder.clear()
      }
    }
  }
}
