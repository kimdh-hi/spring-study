package com.study.udemyspringbatch.service

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Service

@Service
class SecondTasklet: Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        println("Second Tasklet step")
        return RepeatStatus.FINISHED
    }
}