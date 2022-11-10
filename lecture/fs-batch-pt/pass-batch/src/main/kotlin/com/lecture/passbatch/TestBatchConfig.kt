package com.lecture.passbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestBatchConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  @Bean
  fun passStep(): TaskletStep {
    return sbf.get("passStep")
      .tasklet { contribution, chunkContext ->
        println("Execute passStep")
        RepeatStatus.FINISHED
      }
      .build()
  }

  @Bean
  fun passJob(): Job {
    return jbf.get("passJob")
      .start(passStep())
      .build()
  }
}