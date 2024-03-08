package com.lecture.springbatch.itemreader

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.json.GsonJsonObjectReader
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager

//@Configuration
class JsonReaderSampleConfig(
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
    .chunk<JsonReaderTestVO, JsonReaderTestVO>(3, transactionManager)
    .reader(itemReader())
    .writer { log.info("writer > item=$it") }
    .build()

  @Bean
  fun itemReader() = JsonItemReaderBuilder<JsonReaderTestVO>()
    .name("jsonReader")
    .resource(ClassPathResource("/test.json"))
//    .jsonObjectReader(JacksonJsonObjectReader(JsonReaderTestVO::class.java)) // 기본 생성자 필요
    .jsonObjectReader(GsonJsonObjectReader(JsonReaderTestVO::class.java)) // 기본 생성자 x
    .build()
}

data class JsonReaderTestVO(
  val id: Long,
  val name: String,
  val age: Int
)
