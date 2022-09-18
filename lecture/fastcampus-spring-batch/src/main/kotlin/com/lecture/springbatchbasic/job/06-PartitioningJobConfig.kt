package com.lecture.springbatchbasic.job

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.SimplePartitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

@Configuration
class PartitioningJobConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {
  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    private const val PARTITION_SIZE = 100
  }

  @Bean
  fun partitioningJob(masterStep: Step) =
    jbf.get("partitioningJob")
      .incrementer(RunIdIncrementer())
      .start(masterStep)
      .build()

  @Bean
  fun masterStep(
    anotherStep: Step,
    partitioner: Partitioner,
    partitionHandler: TaskExecutorPartitionHandler
  ): Step {
    return sbf.get("masterStep")
      .partitioner("anotherStep", partitioner)
      .partitionHandler(partitionHandler)
      .build()
  }

  @Bean
  fun partitioner(): Partitioner {
    val partitioner = SimplePartitioner()
    partitioner.partition(PARTITION_SIZE)
    return partitioner
  }

  @Bean
  fun partitionHandler(
    anotherStep: Step,
    taskExecutor: TaskExecutor
  ): TaskExecutorPartitionHandler {
    val partitionHandler = TaskExecutorPartitionHandler()
    partitionHandler.step = anotherStep
    partitionHandler.gridSize = PARTITION_SIZE
    partitionHandler.setTaskExecutor(taskExecutor)
    return partitionHandler
  }

}