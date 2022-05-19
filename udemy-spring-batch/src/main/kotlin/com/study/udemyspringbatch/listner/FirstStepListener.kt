package com.study.udemyspringbatch.listner

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component
import javax.batch.api.listener.StepListener

@Component
class FirstStepListener: StepExecutionListener {

    override fun beforeStep(stepExecution: StepExecution) {
        println("Before-Step stepName: ${stepExecution.stepName}")
        println("Before-Step stepExecutionContext: ${stepExecution}")
        println("Before-Step jobExecutionContext: ${stepExecution.jobExecution.executionContext}")

        stepExecution.executionContext.put("sec-key", "sec-value")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        println("After-Step stepName: ${stepExecution.stepName}")
        println("After-Step stepExecutionContext: ${stepExecution}")
        println("After-Step jobExecutionContext: ${stepExecution.jobExecution.executionContext}")

        return null
    }
}