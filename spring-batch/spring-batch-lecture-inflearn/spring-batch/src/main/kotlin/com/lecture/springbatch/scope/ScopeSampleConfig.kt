package com.lecture.springbatch.scope

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ScopeSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1(null))
    .listener(CustomJobListener())
    .build()

  @Bean
  @JobScope
  fun step1(@Value("#{jobParameters['data1']}") data1: String? = null): TaskletStep {
    data1?.let { log.info("step1 data1=$it") }

    return StepBuilder("step1", jobRepository)
      .tasklet(tasklet(null), transactionManager)
      .listener(CustomStepListener())
      .build()
  }

  @Bean
  @StepScope
  fun tasklet(
    @Value("#{jobExecutionContext['data2']}") data2: String? = null,
    @Value("#{stepExecutionContext['data3']}") data3: String? = null,

  ): Tasklet {
    data2?.let { log.info("tasklet data2=$it") }
    data3?.let { log.info("tasklet data3=$it") }

    return Tasklet { _, _ ->
      log.info("tasklet called...")
      RepeatStatus.FINISHED
    }
  }
}