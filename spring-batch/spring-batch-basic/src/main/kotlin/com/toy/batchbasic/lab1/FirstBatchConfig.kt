package com.toy.batchbasic.lab1

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
class FirstBatchConfig(
  private val jobBuilderFactory: JobBuilderFactory,
  private val stepBuilderFactory: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun helloJob(): Job {
    return jobBuilderFactory.get("helloJob")
      .incrementer(RunIdIncrementer()) // job 실행시 parameter id 자동생성
      .start(firstStep()) // job 실행시 최초 실행 메서드
      .build()
  }

  @Bean
  fun firstStep(): Step {
    return stepBuilderFactory.get("helloStep")
      .tasklet { contribution, chunkContext ->
        log.info("firstStep ...")
        RepeatStatus.FINISHED
      }.build()
  }
}