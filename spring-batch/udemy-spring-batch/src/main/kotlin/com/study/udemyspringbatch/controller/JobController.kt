package com.study.udemyspringbatch.controller

import com.study.udemyspringbatch.job.SampleJob
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/job")
@RestController
class JobController(
    private val jobLauncher: JobLauncher,
    private val sampleJob: SampleJob
) {

    @GetMapping("/start/{jobName}")
    fun startJob(@PathVariable jobName: String): String {
        return "Job start ..."
    }

}