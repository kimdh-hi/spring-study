package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.core.domain.PlainText
import com.lecture.springbatchbasic.core.domain.ResultText
import com.lecture.springbatchbasic.core.repository.PlainTextRepository
import com.lecture.springbatchbasic.core.repository.ResultTextRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import java.util.*

@Configuration
class PlainTextJobConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory,
  private val plainTextRepository: PlainTextRepository,
  private val resultTextRepository: ResultTextRepository
) {

  @Bean("plainTextJob")
  fun plainTextJob(plainTextStep: Step): Job = jbf.get("plainTextJob")
    .incrementer(RunIdIncrementer())
    .start(plainTextStep)
    .build()

  @Bean("plainTextStep")
  @JobScope
  fun plainTextStep(
    plainTextReader: ItemReader<PlainText>,
    plainTextProcessor: ItemProcessor<PlainText, String>,
    plainTextWriter: ItemWriter<String>
  ): Step = sbf.get("plainTextStep")
    .chunk<PlainText, String>(5)
    .reader(plainTextReader)
    .processor(plainTextProcessor)
    .writer(plainTextWriter)
    .build()

  @Bean
  @StepScope
  fun plainTextReader(): RepositoryItemReader<PlainText> {
    return RepositoryItemReaderBuilder<PlainText>()
      .name("plainTextBuilder")
      .repository(plainTextRepository)
      .methodName("findAll")
      .pageSize(5)
      .arguments() // pageable 외 메서드에 전달할 인자
      .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
      .build()
  }

  @Bean
  @StepScope
  fun plainTextProcessor(): ItemProcessor<PlainText, String> {
    return ItemProcessor { item -> "processed-${item.text}" }
  }

  @Bean
  @StepScope
  fun plainTextWriter(): ItemWriter<String> {
    return ItemWriter { items ->
      items.forEach { text ->
        val resultText = ResultText(text = text)
        resultTextRepository.save(resultText)
      }
      println("==== chunk end ====")
    }
  }
}