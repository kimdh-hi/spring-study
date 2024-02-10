package com.lecture.springbatch.validator

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
class ValidatorSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  // --job.name=job required1=a required=b // ok
  // --job.name=job required1=a optional=c // failed
  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
//    .validator(CustomJobParametersValidator())
    .validator(DefaultJobParametersValidator(arrayOf("required1", "required2"), arrayOf("optional")))
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

}