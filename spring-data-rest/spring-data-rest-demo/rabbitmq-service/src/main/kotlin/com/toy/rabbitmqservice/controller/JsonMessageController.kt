package com.toy.rabbitmqservice.controller

import com.toy.rabbitmqservice.domain.User
import com.toy.rabbitmqservice.producer.JsonProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class JsonMessageController(
  private val jsonProducer: JsonProducer
) {

  @PostMapping("/pub")
  fun pub(@RequestBody user: User): ResponseEntity<String> {
    jsonProducer.sendJsonMessage(user)
    return ResponseEntity.ok("send message complete message: $user")
  }
}