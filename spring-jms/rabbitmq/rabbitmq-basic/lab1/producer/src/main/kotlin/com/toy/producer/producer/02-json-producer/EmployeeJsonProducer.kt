package com.toy.producer.producer.`02-json-producer`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.producer.domain.Employee
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class EmployeeJsonProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {
  fun sendMessage(employee: Employee) {
    val json = objectMapper.writeValueAsString(employee)
    rabbitTemplate.convertAndSend("test.employee", json)
  }
}