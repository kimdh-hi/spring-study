package com.toy.springstomp.ui.stomp

import com.toy.springstomp.application.chat.ChatService
import com.toy.springstomp.infra.stomp.constants.Destinations
import com.toy.springstomp.infra.stomp.dto.ChatSendRequest
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ChatStompController(
  private val chatService: ChatService,
) {
  @MessageMapping(Destinations.ROOM_SEND_MAPPING)
  fun send(
    @DestinationVariable roomId: String,
    @Payload request: ChatSendRequest,
    principal: Principal,
  ) {
    val user = principal as ChatUser
    chatService.send(user.userId, user.deviceId, roomId, request.text.orEmpty())
  }
}
