package com.lecture.springbatch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.JobParametersIncrementer
import org.springframework.batch.core.JobParametersValidator
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class SimpleJobConfiguration(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
    .next(step2())
    .next(step3())
    .incrementer(RunIdIncrementer()) // jobParameters 자동 증가
    .validator {  } // JobParameter 실행 전 검증
    .preventRestart() // job 실패시에도 재시작 불가 설정
    .listener(object: JobExecutionListener { }) // 특정 시점 콜백


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