package com.toy.springkafka.config

enum class KafkaTopic(
  val topicName: String, val useDlt: Boolean = false,
) {

  TEST("topic.test", true),
}
