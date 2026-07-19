package com.study.quartzscheduler.service

import com.study.quartzscheduler.dto.CronJobRequest
import com.study.quartzscheduler.job.PromptJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Service

@Service
class CronJobService(
  private val scheduler: Scheduler,
) {
  fun register(request: CronJobRequest) {
    val job = JobBuilder.newJob(PromptJob::class.java)
      .withIdentity(request.name)
      .usingJobData("prompt", request.prompt)
      .build()

    val trigger = TriggerBuilder.newTrigger()
      .withIdentity(request.name)
      .withSchedule(CronScheduleBuilder.cronSchedule(request.cronExpression))
      .build()

    scheduler.scheduleJob(job, trigger)
  }
}
