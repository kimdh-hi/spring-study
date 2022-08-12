package com.toy.batchbasic.`lab5-listener`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JobListenerTestBatchConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun listenerTestJob() = jbf.get("listenerTestJob")
    .incrementer(RunIdIncrementer())
    .start(listenerTestStep())
    .listener(InterfaceBaseJobListener())
    .listener(AnnotationBaseJobListener())
    .build()

  private fun listenerTestStep() = sbf.get("listenerTestStep")
    .tasklet { contribution, chunkContext ->
      log.info("step...")
      RepeatStatus.FINISHED
    }
    .listener(AnnotationBaseStepListener())
    .build()
}