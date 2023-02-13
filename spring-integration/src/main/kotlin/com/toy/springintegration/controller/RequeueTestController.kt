package com.toy.springintegration.controller

import com.toy.springintegration.config.activator.RequeueTestMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/requeue")
class RequeueTestController(
  private val rabbitTemplate: RabbitTemplate,
) {

  @PostMapping
  fun send(): String {
    val message = RequeueTestMessage("test")
    rabbitTemplate.convertAndSend("requeueTestQueue", message)
    return "ok"
  }
}