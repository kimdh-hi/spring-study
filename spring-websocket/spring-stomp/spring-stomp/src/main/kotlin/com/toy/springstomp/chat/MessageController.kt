package com.toy.springstomp.chat

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class MessageController(
  private val simpleMessageSendingOperations: SimpMessageSendingOperations
) {

  // WebSocketConfig.registerStompEndpoints 에 설정한 엔드포인트로 웹소켓 연결
  // WebSocketConfig.configureMessageBroker.enableSimpleBroker 에 설정한 엔드포인트를 구독
  // WebSocketConfig.configureMessageBroker.setApplicationDestinationPrefixes 에 설정한 엔드포인트 + @MessageMapping 엔드포인트에서 메시지 처리
  @MessageMapping("/test")
  fun test(chatMessage: ChatMessage) {
    if(chatMessage.isJoinChatMessage())
      chatMessage.setJoinMessage()

    simpleMessageSendingOperations.convertAndSend("/sub/channel/${chatMessage.channelId}", chatMessage.message)
  }
}