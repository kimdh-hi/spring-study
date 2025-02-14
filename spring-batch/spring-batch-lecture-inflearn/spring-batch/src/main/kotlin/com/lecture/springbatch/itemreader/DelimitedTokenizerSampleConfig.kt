package com.lecture.springbatch.itemreader

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager

//@Configuration
class DelimitedTokenizerSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
    .build()

  @Bean
  fun step1() = StepBuilder("step1", jobRepository)
    .chunk<Test2Csv, Test2Csv>(5, transactionManager)
    .reader(itemReader())
    .writer { log.info("writer > item=$it") }
    .build()

  @Bean
  fun itemReader() = FlatFileItemReaderBuilder<Test2Csv>()
    .name("flatFile")
    .resource(ClassPathResource("/test.csv"))
    .fieldSetMapper(TestCvsFieldSetMapperV2())
    .linesToSkip(1)
    .delimited().delimiter(",")
    .names("id", "name", "age")
    .build()
}
