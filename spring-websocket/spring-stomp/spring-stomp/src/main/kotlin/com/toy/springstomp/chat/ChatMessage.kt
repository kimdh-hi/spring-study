package com.toy.springstomp.chat

data class ChatMessage(
  val type: ChatMessageType,
  val sender: String,
  val channelId: String,
  var message: String = ""
) {

  fun isJoinChatMessage(): Boolean {
    return type == ChatMessageType.JOIN
  }

  fun setJoinMessage() {
    message = "$sender 님이 입장했습니다."
  }

  enum class ChatMessageType {
    JOIN, NORMAL
  }
}