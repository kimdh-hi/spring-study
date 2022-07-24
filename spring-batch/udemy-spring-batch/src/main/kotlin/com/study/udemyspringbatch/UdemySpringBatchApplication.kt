package com.study.udemyspringbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class UdemySpringBatchApplication

fun main(args: Array<String>) {
    runApplication<UdemySpringBatchApplication>(*args)
}
