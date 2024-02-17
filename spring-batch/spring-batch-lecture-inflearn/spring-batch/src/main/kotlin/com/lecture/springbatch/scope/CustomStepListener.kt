package com.lecture.springbatch.scope

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener

class CustomStepListener: StepExecutionListener {

  override fun beforeStep(stepExecution: StepExecution) {
    stepExecution.executionContext.putString("data3", "data3value")
  }

  override fun afterStep(stepExecution: StepExecution): ExitStatus? {
    return super.afterStep(stepExecution)
  }
}