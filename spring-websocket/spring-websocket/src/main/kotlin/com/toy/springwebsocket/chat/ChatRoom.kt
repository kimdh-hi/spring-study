package com.toy.springwebsocket.chat

import org.springframework.web.socket.WebSocketSession

data class ChatRoom(
  val roomId: String,
  val name: String,
  val sessions: MutableSet<WebSocketSession> = hashSetOf()
) {

  fun handleAction(session: WebSocketSession, chatMessage: ChatMessage, chatService: ChatService) {
    if(chatMessage.type == ChatMessage.MessageType.ENTER) {
      sessions.add(session)
      chatMessage.message = "${chatMessage.sender} 님이 입장했습니다."
    }
    sendMessage(chatMessage, chatService)
  }

  fun <T> sendMessage(message: T, chatService: ChatService) {
    sessions.parallelStream().forEach { chatService.sendMessage(it, message) }
  }
}