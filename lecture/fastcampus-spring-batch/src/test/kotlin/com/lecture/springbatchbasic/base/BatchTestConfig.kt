package com.lecture.springbatchbasic.base

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
class BatchTestConfig {
}