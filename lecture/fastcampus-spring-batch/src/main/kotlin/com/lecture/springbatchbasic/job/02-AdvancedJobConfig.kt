package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.job.valiator.LocalDateParamValidator
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class AdvancedJobConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun advancedJob(advancedStep: Step): Job = jbf.get("advancedJob")
    .incrementer(RunIdIncrementer())
    .validator(LocalDateParamValidator("targetDate"))
    .start(advancedStep)
    .build()

  @JobScope
  @Bean
  fun advancedStep(advancedTasklet: Tasklet): Step = sbf.get("advancedStep")
    .tasklet(advancedTasklet)
    .build()

  // --spring.batch.job.names=advancedJob -targetDate=2022-01-01
  // jobParameter 에 대한 검증이 필요함 위 경우 targetDate 가 LocalDate 타임인지 검증을 필요로 한다.
  // jobParameter 에 대한 검증은 JobParametersValidator 를 통해 구현한다
  @StepScope
  @Bean
  fun advancedTasklet(@Value("#{jobParameters['targetDate']}") targetDate: String) = Tasklet { contribution, chunkContext ->
    log.info("[advancedTasklet] targetDate: {}", targetDate)
    val paredLocalDate = LocalDate.parse(targetDate)
    log.info("[advancedTasklet] paredLocalDate: {}", paredLocalDate)
    RepeatStatus.FINISHED
  }
}