package com.lecture.passbatch.job.pass

import com.lecture.passbatch.domain.Pass
import com.lecture.passbatch.domain.enums.PassStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory

@Configuration
class ExpirePassesJobConfig(
  private val jobBuilderFactory: JobBuilderFactory,
  private val stepBuilderFactory: StepBuilderFactory,
  private val entityManagerFactory: EntityManagerFactory
) {

  companion object {
    const val CHUNK_SIZE = 5
  }

  @Bean
  fun expirePassesJob(): Job {
    return jobBuilderFactory.get("expirePassesJob")
      .start(expirePassesStep())
      .build()
  }

  @Bean
  fun expirePassesStep(): TaskletStep {
    return stepBuilderFactory.get("expirePassesStep")
      .chunk<Pass, Pass>(CHUNK_SIZE)
      .reader(expirePassesItemReader())
      .processor(expirePassesItemProcessor())
      .writer(expirePassesItemWriter())
      .build()
  }

  @Bean
  @StepScope
  fun expirePassesItemReader(): JpaCursorItemReader<Pass> {
    return JpaCursorItemReaderBuilder<Pass>()
      .name("expirePassesItemReader")
      .entityManagerFactory(entityManagerFactory)
      .queryString("select p from Pass p where p.status = :status and p.endedAt <= :endedAt")
      .parameterValues(
        mapOf(
          "status" to PassStatus.IN_PROGRESS,
          "endedAt" to LocalDateTime.now())
      )
      .build()
  }

  @Bean
  fun expirePassesItemProcessor(): ItemProcessor<Pass, Pass> {
    return ItemProcessor<Pass, Pass> {
      it.status = PassStatus.EXPIRED
      it.expiredAt = LocalDateTime.now()
      it
    }
  }

  @Bean
  fun expirePassesItemWriter(): JpaItemWriter<Pass> {
    return JpaItemWriterBuilder<Pass>()
      .entityManagerFactory(entityManagerFactory)
      .build()
  }
}