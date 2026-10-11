package com.toy.springstomp.infra.amqp.dto

data class DuplicateSessionMessage(
  val deviceId: String,
  val sessionId: String,
)
