package com.lecture.springbatchbasic.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirstJobConfig (
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  @Bean("firstJob")
  fun firstJob(firstStep: Step): Job = jbf.get("firstJob")
    .incrementer(RunIdIncrementer())
    .start(firstStep)
    .build()

  @Bean("firstStep")
  @JobScope
  fun firstStep(tasklet: Tasklet): Step = sbf.get("firstStep")
    .tasklet(tasklet)
    .build()

  @Bean
  @StepScope
  fun tasklet(): Tasklet = Tasklet { contribution, chunkContext ->
    println("tasklet ...")
    RepeatStatus.FINISHED
  }
}