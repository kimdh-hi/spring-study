package com.lecture.springbatch.flowjob

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

//@Configuration
class FlowJobSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1()).on("COMPLETED").to(step3()) //step1 성공시 step3 로
    .from(step1()).on("FAILED").to(step2())// step1 실패시 step2 로
    .end()
    .build()

  @Bean
  fun step1() = StepBuilder("step1", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step1...")
        throw RuntimeException("ex..")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step2() = StepBuilder("step2", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step2...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step3() = StepBuilder("step3", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step3...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

}