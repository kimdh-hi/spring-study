package com.lecture.springbatch.tasklet

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.job.DefaultJobParametersValidator
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class TaskletSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
    .next(step2())
    .build()

  @Bean
  fun step1() = StepBuilder("step1", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step1...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step2() = StepBuilder("step2", jobRepository)
    .tasklet(CustomTasklet(), transactionManager)
    .build()
}