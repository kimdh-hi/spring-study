package com.lecture.springbatch.incrementer

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersIncrementer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomJobParametersIncrementer: JobParametersIncrementer {

  override fun getNext(parameters: JobParameters?): JobParameters {
    val date = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
    return JobParametersBuilder()
      .addString("custom.runId", date)
      .toJobParameters()
  }
}