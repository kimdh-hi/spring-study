package com.toy.springbatchplus

import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class TestJobBatchPlusConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun testJob2(batch: BatchDsl, transactionManager: PlatformTransactionManager): Job = batch {
    job("testJob2") {
      step("testStep1") {
        tasklet({ _, _ ->
          log.info("testJob2 - testStep1...")
          RepeatStatus.FINISHED }, transactionManager)
      }
    }
  }
}