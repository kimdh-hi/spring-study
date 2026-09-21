package com.toy.springrawwebsocket.domain.chat.model

data class DeviceSession(
  val sessionId: String,
  val deviceId: String,
  val userId: String,
)
