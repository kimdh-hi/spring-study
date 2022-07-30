package com.toy.consumer.consumer.`02-json_consumer`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.consumer.domain.Employee
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class EmployeeJsonConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["test.employee"])
  fun listener(message: String) {
    val employee = objectMapper.readValue(message, Employee::class.java)
    log.info("employee: {}", employee)
  }
}