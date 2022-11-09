package com.lecture.springbatchbasic

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableBatchProcessing
class SpringBatchBasicApplication

fun main(args: Array<String>) {
	runApplication<SpringBatchBasicApplication>(*args)
}
