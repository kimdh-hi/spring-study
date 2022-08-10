package com.toy.batchbasic.`lab3-chunk_base_step`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class `Lab3JobConfig`(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun itemsProcessingJob(): Job = jbf.get("itemsProcessingJob")
    .incrementer(RunIdIncrementer())
    .start(taskBaseStep())
    .next(chunkBaseStep(null))
    .build()

  @Bean
  @JobScope
  fun chunkBaseStep(@Value("#{jobParameters[chunkSize]}") chunkSize: String?) = sbf.get("chunkBaseStep")
    .chunk<String, String>(chunkSize!!.toInt())
    .reader(itemReader())
    .processor(itemProcessor())
    .writer(itemWriter())
    .build()

  private fun itemWriter() = ItemWriter<String> { items ->
    log.info("chunk item size: {}", items.size)
//    items.forEach {
//      log.info(it)
//    }
  }


  private fun itemProcessor() =  ItemProcessor { item:String ->
    item + "some str ..."
  }

  private fun itemReader(): ItemReader<out String>  {
    val items = getItems()
    log.info("chunk itemReader ... size: {}", items.size)
    return ListItemReader(items)
  }

  fun taskBaseStep() = sbf.get("taskBaseStep")
    .tasklet(tasklet())
    .build()

  private fun tasklet() = Tasklet { contribution, chunkContext ->
    val items = getItems()
    log.info("items size: {}", items.size)
    RepeatStatus.FINISHED
  }

  private fun getItems(): MutableList<String> {
    val list = mutableListOf<String>()
    for (i in 1..100) {
      list.add("str$i")
    }
    return list
  }
}