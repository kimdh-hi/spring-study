package com.study.udemyspringbatch.config

import com.study.udemyspringbatch.listner.FirstJobListener
import com.study.udemyspringbatch.listner.FirstStepListener
import com.study.udemyspringbatch.service.SecondTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SampleJob(
    val jbf: JobBuilderFactory, val sbf: StepBuilderFactory,
    val secondTasklet: SecondTasklet,
    val firstJobListener: FirstJobListener, val firstStepListener: FirstStepListener) {

    @Bean
    fun firstJob(): Job {
        return jbf.get("First Job Name")
            .incrementer(RunIdIncrementer()) // parameter for unique job instance id
            .start(firstStep())
            .next(secondStep()) // start -> next -> next ... sequential
            .listener(firstJobListener)
            .build()
    }

    private fun firstStep(): Step {
        return sbf.get("First Step Name")
            .tasklet(firstTasklet())
            .listener(firstStepListener)
            .build()
    }

    private fun firstTasklet(): Tasklet {
        return Tasklet { contribution, chunkContext ->
            println("First Tasklet step")
            println("First Tasklet step-execution-context: ${chunkContext.stepContext.stepExecutionContext}")
            RepeatStatus.FINISHED
        }
    }

    private fun secondStep(): Step {
        return sbf.get("Second Step Name")
            .tasklet(secondTasklet)
            .build()
    }

//    private fun secondTasklet(): Tasklet {
//        return Tasklet { contribution, chunkContext ->
//            println("Second Tasklet step")
//            RepeatStatus.FINISHED
//        }
//    }
}