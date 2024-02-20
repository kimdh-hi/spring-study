package com.lecture.springbatch.chunk

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.support.ListItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

//@Configuration
class ChunkSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
    .build()

  @Bean
  @JobScope
  fun step1() = StepBuilder("step1", jobRepository)
    .chunk<String, String>(5, transactionManager)
    .reader(ListItemReader(arrayListOf("item1", "item2", "item3", "item4", "item5", "item6")))
    .processor {
      log.info("processor item=$it")
      "$it processed"
    }
    .writer {
      log.info("written item=$it")
    }
    .build()
}