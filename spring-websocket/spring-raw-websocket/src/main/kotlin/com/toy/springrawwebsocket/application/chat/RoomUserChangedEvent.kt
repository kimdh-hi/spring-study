package com.toy.springrawwebsocket.application.chat

data class RoomUserChangedEvent(
  val userId: String,
  val roomId: String,
  val joined: Boolean,
)
