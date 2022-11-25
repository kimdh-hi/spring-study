package com.toy.springintegrationdemo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springintegrationdemo.config.activator.RequeueTestMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/requeue")
class RequeueTestController(
  private val rabbitTemplate: RabbitTemplate,
) {

  @PostMapping
  fun send(@RequestBody requestVO: RequeueTestRequestVO): String {
    val message = RequeueTestMessage(requestVO.data)
    rabbitTemplate.convertAndSend("requeueTestQueue", message)
    return "ok"
  }
}

data class RequeueTestRequestVO(
  val data: String? = null
)