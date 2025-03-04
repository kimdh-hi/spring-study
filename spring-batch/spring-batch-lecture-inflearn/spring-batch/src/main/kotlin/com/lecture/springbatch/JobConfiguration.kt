package com.lecture.springbatch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

//@Configuration
class JobConfiguration(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  /**
   * job,step 생성 debug: SimpleJobBuilder
   * job,step 실행 debug: TaskExecutorJobLauncher, SimpleJobLauncher, SimpleJob.execute
   */
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
    .tasklet(
      { _, _ ->
        log.info("step2...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()
}