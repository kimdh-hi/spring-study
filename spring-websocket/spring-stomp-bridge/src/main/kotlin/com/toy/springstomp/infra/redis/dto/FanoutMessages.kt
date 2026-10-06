package com.toy.springstomp.infra.redis.dto

data class DuplicateSessionMessage(
  val deviceId: String,
  val sessionId: String,
)
