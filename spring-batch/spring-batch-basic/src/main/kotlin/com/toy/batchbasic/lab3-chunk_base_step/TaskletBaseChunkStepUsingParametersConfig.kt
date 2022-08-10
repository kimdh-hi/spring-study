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
class TaskletBaseChunkStepUsingParametersConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  /*
  tasklet 을 chunk 처럼 ..
  동일한 step 내에서 공유 가능한 stepExecutionContext 를 사용해서 대상 아이템들을 슬라이싱

  jobParameters 사용
  Program arguments: -chunkSize=20 --jobName=taskletChunkJob2
   */
  @Bean
  fun taskletChunkStyleJob2() = jbf.get("taskletChunkJob2")
    .incrementer(RunIdIncrementer())
    .start(taskletChunkStyleStep())
    .build()

  private fun taskletChunkStyleStep() = sbf.get("taskletChunkStep2")
    .tasklet(tasklet())
    .build()

  private fun tasklet(): Tasklet {
    val items = getItems()

    return Tasklet { contribution, chunkContext ->
      val stepExecution = contribution.stepExecution
      val jobParameters =  stepExecution.jobParameters
      val chunkSize = jobParameters.getString("chunkSize", "10")!!.toInt()

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