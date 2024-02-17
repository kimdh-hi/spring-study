package com.lecture.springbatch.scope

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener

class CustomJobListener: JobExecutionListener {

  override fun beforeJob(jobExecution: JobExecution) {
    jobExecution.executionContext.putString("data2", "data2value")
  }

  override fun afterJob(jobExecution: JobExecution) {
    super.afterJob(jobExecution)
  }
}