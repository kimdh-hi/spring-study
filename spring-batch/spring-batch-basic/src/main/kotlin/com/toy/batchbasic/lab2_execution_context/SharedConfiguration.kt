package com.toy.batchbasic.lab2_execution_context

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SharedConfiguration(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun shareJob(): Job = jbf.get("shareJob")
    .incrementer(RunIdIncrementer())
    .start(shareStep1())
    .next(shareStep2())
    .build()

  private fun shareStep1(): Step = sbf.get("shareStep1")
    .tasklet { contribution, chunkContext ->
      val stepExecution = contribution.stepExecution
      val stepExecutionContext = stepExecution.executionContext
      stepExecutionContext.putString("stepKey", "value-from-shareStep1")

      // jobExecutionContext - step 간 데이터 공유 가능
      val jobExecution = stepExecution.jobExecution
      val jobExecutionContext = jobExecution.executionContext
      jobExecutionContext.putString("jobKey", "value-from-shareStep1")

      // stepExecutionContext - step 간 데이터 공유 불가능 (step 내에서만 공유 가능)
      val jobInstance = jobExecution.jobInstance
      val jobParameters = jobExecution.jobParameters
      log.info("jobName: {}, stepName: {}, parameter.runId: {}",
        jobInstance.jobName,
        stepExecution.stepName,
        jobParameters.getLong("run.id")
      )

      RepeatStatus.FINISHED
    }.build()

  private fun shareStep2(): Step = sbf.get("shareStep2")
    .tasklet { contribution, chunkContext ->
      val stepExecution = contribution.stepExecution
      val stepExecutionContext = stepExecution.executionContext

      val jobExecution = stepExecution.jobExecution
      val jobExecutionContext = jobExecution.executionContext

      log.info("jobKey={}, stepKey={}",
        jobExecutionContext.getString("jobKey", "isEmpty..."),
        stepExecutionContext.getString("stepKey", "isEmpty...")
      )

      RepeatStatus.FINISHED
    }.build()

}

