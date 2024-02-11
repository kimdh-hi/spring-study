package com.lecture.springbatch.tasklet

import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class CustomTasklet: Tasklet {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
    log.info("CustomTasklet called...")
    return RepeatStatus.FINISHED
  }
}