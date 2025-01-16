package com.toy.kotlinlogging.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LoggingServiceTest @Autowired constructor(
 private val loggingService: LoggingService
) {

 @Test
 fun `stringTemplateLogging`() {
  loggingService.stringTemplateLogging("data")
 }

 @Test
 fun `kotlinLogging`() {
  loggingService.kotlinLogging("data")
 }

 @Test
 fun `kotlinErrorLogging`() {
  loggingService.kotlinErrorLogging("error")
 }
}
