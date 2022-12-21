package com.toy.springwebsocket.chat

data class ChatRoomResponseVO(
  val roomId: String,
  val name: String,
  val sessions: List<String>
)