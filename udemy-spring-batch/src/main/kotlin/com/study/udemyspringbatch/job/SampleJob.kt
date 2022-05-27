package com.study.udemyspringbatch.job

import com.study.udemyspringbatch.job.chunk.MyItemProcessor
import com.study.udemyspringbatch.job.chunk.MyItemReader
import com.study.udemyspringbatch.job.chunk.MyItemWriter
import com.study.udemyspringbatch.listener.FirstJobListener
import com.study.udemyspringbatch.listener.FirstStepListener
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
    val firstJobListener: FirstJobListener, val firstStepListener: FirstStepListener,
    val myItemReader: MyItemReader, val myItemProcessor: MyItemProcessor, val myItemWriter: MyItemWriter
) {

    @Bean
    fun firstJob(): Job {
        return jbf.get("First Job")
            .incrementer(RunIdIncrementer()) // parameter for unique job instance id
            .start(firstStep())
            .next(secondStep()) // start -> next -> next ... sequential
            .listener(firstJobListener)
            .build()
    }

    private fun firstStep(): Step {
        return sbf.get("First Step")
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

    @Bean
    fun secondJob(): Job {
        return jbf.get("Second Job")
            .incrementer(RunIdIncrementer())
            .start(chunkStep())
            .build()
    }

    private fun chunkStep(): Step {
        return sbf.get("Chunk Step")
            .chunk<Int, Long>(3)
            .reader(myItemReader)
            .processor(myItemProcessor)
            .writer(myItemWriter)
            .build()
    }
}