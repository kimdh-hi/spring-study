package com.study.quartzscheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuartzSchedulerApplication

fun main(args: Array<String>) {
  runApplication<QuartzSchedulerApplication>(*args)
}
