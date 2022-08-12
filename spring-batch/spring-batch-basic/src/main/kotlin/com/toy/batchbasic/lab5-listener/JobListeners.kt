package com.toy.batchbasic.`lab5-listener`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.annotation.AfterJob
import org.springframework.batch.core.annotation.BeforeJob

class InterfaceBaseJobListener: JobExecutionListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun beforeJob(jobExecution: JobExecution) {
    log.info("interfaceBase beforeJob...")
  }

  override fun afterJob(jobExecution: JobExecution) {
    log.info("interfaceBase afterJob...")
  }
}

class AnnotationBaseJobListener {

  private val log = LoggerFactory.getLogger(javaClass)

  @BeforeJob
  fun beforeJob(jobExecution: JobExecution) {
    log.info("annotationBase beforeJob...")
  }

  @AfterJob
  fun afterJob(jobExecution: JobExecution) {
    log.info("annotationBase afterJob...")
  }
}

class AnnotationBaseStepListener: StepExecutionListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun beforeStep(stepExecution: StepExecution) {
    log.info("beforeStep...")
  }

  override fun afterStep(stepExecution: StepExecution): ExitStatus? {
    log.info("afterStep... writeCount: {}", stepExecution.writeCount)
    return stepExecution.exitStatus
  }
}