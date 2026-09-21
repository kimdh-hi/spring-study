package com.toy.springrawwebsocket.infra.redis.dto

data class RoomUserChangedMessage(
  val userId: String,
  val roomId: String,
  val joined: Boolean,
)

data class DuplicateSessionMessage(
  val deviceId: String,
  val sessionId: String,
)
