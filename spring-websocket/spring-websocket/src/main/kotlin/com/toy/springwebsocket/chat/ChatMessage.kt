package com.toy.springwebsocket.chat

data class ChatMessage(
  val roomId: String,
  val sender: String,
  var message: String,
  val type: MessageType
) {

  enum class MessageType {
    ENTER, NORMAL
  }
}