package com.toy.batchbasic

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class BatchBasicApplication

fun main(args: Array<String>) {
	runApplication<BatchBasicApplication>(*args)
}
