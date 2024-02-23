package com.lecture.springbatch.itemreader

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.Range
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class FixedLengthTokenizerSampleConfig(
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
    .resource(ClassPathResource("/test2.csv"))
    .fieldSetMapper(BeanWrapperFieldSetMapper())
    .targetType(Test2Csv::class.java)
    .linesToSkip(1)
    .fixedLength()
    .addColumns(Range(1, 5))
    .addColumns(Range(6, 9))
    .addColumns(Range(10, 11))
    .names("name", "year", "age")
    .build()
}

// 기본 생성자, var 필요
data class Test2Csv(
  var name: String = "",
  var year: Int = 0,
  var age: Int = 0
)