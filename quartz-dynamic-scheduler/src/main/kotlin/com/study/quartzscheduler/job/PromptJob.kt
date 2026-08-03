package com.study.quartzscheduler.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

@DisallowConcurrentExecution
class PromptJob : Job {
  private val log = LoggerFactory.getLogger(PromptJob::class.java)

  override fun execute(context: JobExecutionContext) {
    val prompt = context.mergedJobDataMap.getString("prompt")
    log.info("[{}] {} run prompt: {}", context.scheduler.schedulerInstanceId, context.jobDetail.key.name, prompt)
  }
}
