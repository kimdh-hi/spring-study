package com.study.udemyspringbatch.listner

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class FirstJobListener: JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
        println("Before-Job jobName: ${jobExecution.jobInstance.jobName}")
        println("Before-Job jobParameters: ${jobExecution.jobParameters}")
        println("Before-Job jobExecutionContext: ${jobExecution.executionContext}")

        jobExecution.executionContext.put("jec-key", "jec-value")
    }

    override fun afterJob(jobExecution: JobExecution) {
        println("After-Job jobName: ${jobExecution.jobInstance.jobName}")
        println("After-Job jobParameters: ${jobExecution.jobParameters}")
        println("After-Job jobExecutionContext: ${jobExecution.executionContext}")
    }


}