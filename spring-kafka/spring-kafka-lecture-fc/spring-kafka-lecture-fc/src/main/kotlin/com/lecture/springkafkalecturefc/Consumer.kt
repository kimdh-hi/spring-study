package com.lecture.springkafkalecturefc

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class Consumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @KafkaListener(id = "fc-id", topics = ["quick-start"])
  fun listen(message: String) {
    println("==========")
    println(message)
  }

}