package com.toy.springbatchplus

import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import org.springframework.batch.core.Job
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

@Bean
fun testJob(batch: BatchDsl): Job = batch {
  job("testJob") {
    step("subJob1Step") {
      jobBean("subJob1")
    }
    step("subJob2Step") {
      jobBean("subJob2")
    }
  }
}

@Bean
fun subJob1(
  batch: BatchDsl,
  transactionManager: PlatformTransactionManager
): Job = batch {
  job("subJob1") {
    step("subJob1 - step1") {
      tasklet({ _, _ -> RepeatStatus.FINISHED }, transactionManager)
    }
    step("subJob1 - step2") {
      tasklet({ _, _ -> RepeatStatus.FINISHED }, transactionManager)
    }
  }
}

@Bean
fun subJob2(
  batch: BatchDsl,
  transactionManager: PlatformTransactionManager
): Job = batch {
  job("subJob2") {
    step("subJob2 - step1") {
      tasklet({ _, _ -> RepeatStatus.FINISHED }, transactionManager)
    }
    step("subJob2 - step2") {
      tasklet({ _, _ -> RepeatStatus.FINISHED }, transactionManager)
    }
  }
}