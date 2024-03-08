package com.lecture.springbatch.itemreader

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

private const val CHUNK_SIZE = 10

@Configuration
class JdbcCursorItemReaderSampleConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
  private val dataSource: DataSource
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
    .build()

  @Bean
  fun step1() = StepBuilder("step1", jobRepository)
    .chunk<JdbcCursorItemReaderTarget, JdbcCursorItemReaderTarget>(CHUNK_SIZE, transactionManager)
    .reader(itemReader())
    .writer(itemWriter())
    .build()

  @Bean
  fun itemReader() = JdbcCursorItemReaderBuilder<JdbcCursorItemReaderTarget>()
    .name("jdbcCursorItemReader")
    .fetchSize(CHUNK_SIZE)
    .sql("select id, name from table where name like ? order by name")
    .beanRowMapper(JdbcCursorItemReaderTarget::class.java)
    .queryArguments("kim%")
    .dataSource(dataSource)
    .build()

  @Bean
  fun itemWriter() = ItemWriter<JdbcCursorItemReaderTarget> {
    it.items.forEach { item ->
      log.info("itemWriter > name=${item.name}")
    }
  }
}

data class JdbcCursorItemReaderTarget(
  val id: Long,
  val name: String,
)