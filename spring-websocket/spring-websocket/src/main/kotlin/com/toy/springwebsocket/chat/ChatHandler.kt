package com.toy.springwebsocket.chat

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatHandler(
  private val objectMapper: ObjectMapper,
  private val chatService: ChatService
): TextWebSocketHandler() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    val payload = message.payload
    log.info("payload={}", payload)

    val chatMessage = objectMapper.readValue(payload, ChatMessage::class.java)
    val chatRoom = chatService.findRoomById(chatMessage.roomId)
    chatRoom?.handleAction(session, chatMessage, chatService)
  }
}