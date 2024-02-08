package com.lecture.springbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*

//@Component
class JobRunner(
  private val jobLauncher: JobLauncher,
  private val job: Job
): ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    val jobParameters = JobParametersBuilder()
      .addString("name", "name2")
      .addLong("seq", 1L)
      .addDate("date", Date())
      .addDouble("double", 1.1)
      .toJobParameters()

    jobLauncher.run(job, jobParameters)
  }
}