package com.toy.rabbitmqservice.controller

import com.toy.rabbitmqservice.producer.RabbitmqProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class MessageController(
  private val rabbitmqProducer: RabbitmqProducer
) {

  @GetMapping("/pub")
  fun sendMessage(@RequestParam message: String): ResponseEntity<String> {
    rabbitmqProducer.sendMessage(message)
    return ResponseEntity.ok("ok")
  }
}