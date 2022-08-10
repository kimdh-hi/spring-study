package com.toy.batchbasic.`lab3-chunk_base_step`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class `TaskletBaseChunkStepUsingParametersV2Config`(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun taskletChunkStyleJob3() = jbf.get("taskletChunkJob3")
    .incrementer(RunIdIncrementer())
    .start(taskletChunkStyleStep())
    .build()

  private fun taskletChunkStyleStep() = sbf.get("taskletChunkStep3")
    .tasklet(taskletV2(null))
    .build()

  @Bean
  @StepScope
  fun taskletV2(@Value("#{jobParameters[chunkSize]}") chunkSize: String?): Tasklet {
    val items = getItems()

    return Tasklet { contribution, chunkContext ->
      val stepExecution = contribution.stepExecution

      val parsedChunkSize = chunkSize?.toInt() ?: 10

      val readFromIdx = stepExecution.readCount
      val readToIdx = readFromIdx + parsedChunkSize

      if(readFromIdx >= items.size){
        RepeatStatus.FINISHED
      } else {
        val subItems = items.subList(readFromIdx, readToIdx)
        log.info("item size: {}", subItems.size)

        stepExecution.readCount = readToIdx
        RepeatStatus.CONTINUABLE
      }
    }
  }

  private fun getItems(): MutableList<String> {
    val list = mutableListOf<String>()
    for (i in 1..100) {
      list.add("str$i")
    }
    return list
  }
}