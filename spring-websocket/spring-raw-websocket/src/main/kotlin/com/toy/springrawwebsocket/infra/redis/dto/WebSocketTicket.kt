package com.toy.springrawwebsocket.infra.redis.dto

data class WebSocketTicket(
  val userId: String,
  val deviceId: String,
)
