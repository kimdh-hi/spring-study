package com.toy.springscheduler.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar


@Configuration
@EnableScheduling
@EnableAsync
class SchedulerConfig: SchedulingConfigurer {

  override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
//    val threadPool = ThreadPoolTaskScheduler().apply {
//      poolSize = Runtime.getRuntime().availableProcessors()
//      initialize()
//    }
//
//    taskRegistrar.setTaskScheduler(threadPool)
  }
}