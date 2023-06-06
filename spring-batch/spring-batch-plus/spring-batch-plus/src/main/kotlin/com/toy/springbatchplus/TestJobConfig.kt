package com.toy.springbatchplus

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class TestJobConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun testJob1(): Job {
    return JobBuilder("testJob1", jobRepository)
      .start(
        StepBuilder("testStep1", jobRepository)
          .tasklet({ _, _ ->
            log.info("testJob1 - testStep1...")
            RepeatStatus.FINISHED
          }, transactionManager)
          .build()
      )
      .build()
  }
}