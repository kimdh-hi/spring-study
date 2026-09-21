package com.toy.springrawwebsocket.infra.ws

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage
import com.toy.springrawwebsocket.infra.redis.RedisChannels
import com.toy.springrawwebsocket.infra.redis.RedisPublisher
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage

@Component
class MessageSender(
  private val localSessionRegistry: SessionRegistry,
  private val sessionSender: SessionSender,
  private val publisher: RedisPublisher,
  private val wireProtocol: WireProtocol,
) {
  fun sendToRoom(message: ChatMessage) {
    publisher.publishRaw(RedisChannels.room(message.roomId), wireProtocol.toMessageFrame(message))
  }

  fun sendToRoomLocally(roomId: String, frame: String) {
    val entries = localSessionRegistry.findEntriesByRoomId(roomId)
    if (entries.isEmpty()) return
    val message = TextMessage(frame)
    entries.forEach { sessionSender.send(it, message) }
  }
}
