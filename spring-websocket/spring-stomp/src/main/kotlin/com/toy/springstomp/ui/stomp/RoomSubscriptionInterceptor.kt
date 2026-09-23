package com.toy.springstomp.ui.stomp

import com.toy.springstomp.application.chat.RoomUserService
import com.toy.springstomp.infra.stomp.constants.Destinations
import com.toy.springstomp.infra.stomp.error.ErrorCode
import com.toy.springstomp.infra.stomp.error.StompException
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class RoomSubscriptionInterceptor(
  private val roomUserService: RoomUserService,
) : ChannelInterceptor {
  override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
    val accessor = StompHeaderAccessor.wrap(message)
    if (accessor.command != StompCommand.SUBSCRIBE) return message
    val roomId = Destinations.roomIdOf(accessor.destination.orEmpty()) ?: return message
    val userId = accessor.user?.name ?: throw StompException(ErrorCode.NOT_A_ROOM_USER)
    if (!roomUserService.isMember(roomId, userId)) throw StompException(ErrorCode.NOT_A_ROOM_USER)

    return message
  }
}
