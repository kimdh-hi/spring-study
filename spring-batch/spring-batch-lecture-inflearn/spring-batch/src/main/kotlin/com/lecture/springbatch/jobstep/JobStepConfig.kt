package com.lecture.springbatch.jobstep

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class JobStepConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
  private val jobLauncher: JobLauncher
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun parentJob() = JobBuilder("parentJob", jobRepository)
    .start(jobStep())
    .next(step2())
    .build()

  @Bean
  fun jobStep() = StepBuilder("jobStep", jobRepository)
    .job(childJob())
    .launcher(jobLauncher)
    .build()

  @Bean
  fun childJob() = JobBuilder("childJob", jobRepository)
    .start(childJobStep())
    .build()

  @Bean
  fun childJobStep() = StepBuilder("childJobStep", jobRepository)
    .tasklet( { _, _ ->
      log.info("childJobStep called...")
      RepeatStatus.FINISHED
    }, transactionManager)
    .build()

  @Bean
  fun step2() = StepBuilder("step2", jobRepository)
    .tasklet( { _, _ ->
      log.info("step2 called...")
      RepeatStatus.FINISHED
    }, transactionManager)
    .build()
}