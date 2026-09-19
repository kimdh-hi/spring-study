package com.toy.springrawwebsocket.infra.redis

import com.toy.springrawwebsocket.infra.ws.MessageSender
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class RoomFrameListener(
  private val messageSender: MessageSender,
) : MessageListener {
  override fun onMessage(message: Message, pattern: ByteArray?) {
    val roomId = RedisChannels.roomIdOf(String(message.channel)) ?: return
    messageSender.sendToRoomLocally(roomId, String(message.body))
  }
}
