package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.core.domain.AmountFieldSetMapper
import com.lecture.springbatchbasic.core.domain.AmountVO
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import java.io.File

@Configuration
class `04-MultiThreadStepJobConfig`(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun multiThreadStepJob(multiThreadStep: Step) =
    jbf.get("multiThreadStepJob")
      .incrementer(RunIdIncrementer())
      .start(multiThreadStep)
      .build()

  @JobScope
  @Bean
  fun multiThreadStep(
    amountItemReader: ItemReader<AmountVO>,
    amountItemProcessor: ItemProcessor<AmountVO, AmountVO>,
    amountItemWriter: ItemWriter<AmountVO>,
    taskExecutor: TaskExecutor
  ) =
    sbf.get("multiThreadStep")
      .chunk<AmountVO, AmountVO>(10)
      .reader(amountItemReader)
      .processor(amountItemProcessor)
      .writer(amountItemWriter)
      .taskExecutor(taskExecutor) // multi-thread step
      .build()

  // 멀티쓰레드로 동작
  // 한 개 쓰레드드가 한 개 chunkSize 를 처리
  @Bean
  fun taskExecutor(): TaskExecutor {
    val taskExecutor = SimpleAsyncTaskExecutor("async-task-executor")
    taskExecutor.concurrencyLimit = 4
    return taskExecutor
  }

  @StepScope
  @Bean
  fun amountItemReader() = FlatFileItemReaderBuilder<AmountVO>()
    .name("amountReader")
    .fieldSetMapper(AmountFieldSetMapper())
    .lineTokenizer(DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB))
    .resource(FileSystemResource("test/input.txt"))
    .build()

  @StepScope
  @Bean
  fun amountItemProcessor() = ItemProcessor<AmountVO, AmountVO> {
    log.info("item: {}-{}", it, Thread.currentThread().name)
    it.amount = it.amount * 100
    it
  }

  @StepScope
  @Bean
  fun amountItemWriter(): FlatFileItemWriter<AmountVO> {
    val fieldExtractor = BeanWrapperFieldExtractor<AmountVO>()
    fieldExtractor.setNames(arrayOf("id", "name", "amount"))
    fieldExtractor.afterPropertiesSet()

    val lineAggregator = DelimitedLineAggregator<AmountVO>()
    lineAggregator.setFieldExtractor(fieldExtractor)

    val outputPath = "test/output.txt"
    File(outputPath).createNewFile()

    return FlatFileItemWriterBuilder<AmountVO>()
      .name("amountItemWriter")
      .resource(FileSystemResource(outputPath))
      .lineAggregator(lineAggregator)
      .build()
  }
}