package com.lecture.passbatch.job.pass

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class AddPassesJobConfig(
  private val jobBuilderFactory: JobBuilderFactory,
  private val stepBuilderFactory: StepBuilderFactory,
  private val addPassesTasklet: AddPassesTasklet
) {

  @Bean
  fun addPassesJob() {
    return jobBuilderFactory.get("addPassesJob")
      .start(addPassesStep())
      .build()
  }

  @Bean
  fun addPassesStep(): Step {
    return stepBuilderFactory.get("addPassesStep")
      .tasklet(addPassesTasklet)
      .build()
  }
}