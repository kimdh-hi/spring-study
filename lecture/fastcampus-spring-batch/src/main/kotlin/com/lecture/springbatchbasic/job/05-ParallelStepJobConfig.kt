package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.core.domain.AmountVO
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.job.flow.support.SimpleFlow
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

/**
 * batch 가 제공하는 Flow 를 기반으로 step 을 멀티 쓰레드로 실행
 *
 * Flow 에 여러 step 을 지정하여 parallel 하게 실행시킨다.
 * 단, 한 개 step 은 한 개 쓰레드에서 실행된다.
 * 한 개 step 을 한 개 쓰레드에서만 실행시키고 동시에 여러 step 을 병렬적으로 실행시키고 싶다면 고려해보자.
 */
@Configuration
class ParallelStepJobConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun parallelJob(splitFlow: Flow): Job =
    jbf.get("parallelJob")
      .incrementer(RunIdIncrementer())
      .start(splitFlow)
      .build()
      .build()

  @Bean
  fun splitFlow(
    taskExecutor: TaskExecutor,
    flowAmountFileStep: Flow,
    flowAnotherStep: Flow
  ): Flow =
    FlowBuilder<SimpleFlow>("splitFlow")
      .split(taskExecutor)
      .add(flowAmountFileStep, flowAnotherStep)
      .build()

  @Bean
  fun flowAmountFileStep(amountFileStep: Step): Flow =
    FlowBuilder<SimpleFlow>("flowAmountFileStep")
      .start(amountFileStep)
      .end()

  @Bean
  fun amountFileStep(
    amountItemReader: ItemReader<AmountVO>,
    amountItemProcessor: ItemProcessor<AmountVO, AmountVO>,
    amountItemWriter: ItemWriter<AmountVO>,
  ) = sbf.get("amountFileStep")
      .chunk<AmountVO, AmountVO>(10)
      .reader(amountItemReader)
      .processor(amountItemProcessor)
      .writer(amountItemWriter)
      .build()

  @Bean
   fun flowAnotherStep(anotherStep: Step): Flow =
     FlowBuilder<SimpleFlow>("anotherStep")
       .start(anotherStep)
       .build()

  @Bean
  fun anotherStep(): Step =
    sbf.get("anotherStep")
      .tasklet { contribution, chunkContext ->
        Thread.sleep(500)
        log.info("[Another step]\t Thread: {}", Thread.currentThread().name)
        RepeatStatus.FINISHED
      }.build()

}