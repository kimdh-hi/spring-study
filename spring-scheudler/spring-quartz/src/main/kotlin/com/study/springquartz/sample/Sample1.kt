package com.study.springquartz.sample

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class SampleJob: Job {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun execute(context: JobExecutionContext) {
    log.info("sampleJob...")
  }
}

class Sample