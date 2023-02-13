package com.toy.springintegration.controller

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serial
import java.io.Serializable

@RestController
@RequestMapping("/api/route")
class RouteTestController(
  private val rabbitTemplate: RabbitTemplate,
) {

  @PostMapping
  fun send(@RequestBody message: RouteTestMessage): String {
    rabbitTemplate.convertAndSend("typeBaseRouteTestQueue", message)
    return "ok"
  }
}

data class RouteTestMessage(
  val data: String,
  val type: RouteType
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -8663105188338163042L
  }
}

enum class RouteType {
  TYPE1, TYPE2
}