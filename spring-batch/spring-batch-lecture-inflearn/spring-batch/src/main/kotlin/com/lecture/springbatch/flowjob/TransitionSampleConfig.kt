package com.lecture.springbatch.flowjob

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

//@Configuration
class TransitionSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {


  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
      .on("FAILED")
      .to(step2())
      .on("FAILED")
      .stop()
    .from(step1())
      .on("*")
      .to(step3())
      .next(step4())
    .from(step2())
      .on("*")
      .to(step5())
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
//        throw RuntimeException("ex..")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step3() = StepBuilder("step3", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step3...")
        throw RuntimeException("ex..")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step4() = StepBuilder("step4", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step4...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun step5() = StepBuilder("step5", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step5...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()
}