package com.toy.batchbasic.`lab3-chunk_base_step`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class `TaskletBaseChunkStepConfig`(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  /*
  tasklet 을 chunk 처럼 ..
  동일한 step 내에서 공유 가능한 stepExecutionContext 를 사용해서 대상 아이템들을 슬라이싱
  stepExecution 에 이미 정의된 (0으로 초기화) readCount 를 사용함

  chunk 기반 step 보다 직접 작성해야 하는 코드양이 많음
  슬라이싱이 필요한 배치 작업이라면 chunk step 이 좋을 듯 하다.
   */
  @Bean
  fun taskletChunkStyleJob() = jbf.get("taskletChunkJob")
    .incrementer(RunIdIncrementer())
    .start(taskletChunkStyleStep())
    .build()

  private fun taskletChunkStyleStep() = sbf.get("taskletChunkStep")
    .tasklet(tasklet())
    .build()

  private fun tasklet(): Tasklet {
    val items = getItems()

    return Tasklet { contribution, chunkContext ->
      val chunkSize = 10
      val stepExecution = contribution.stepExecution
      val readFromIdx = stepExecution.readCount
      val readToIdx = readFromIdx + chunkSize

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