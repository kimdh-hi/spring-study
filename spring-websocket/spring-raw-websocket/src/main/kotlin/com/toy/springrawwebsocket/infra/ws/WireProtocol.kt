package com.toy.springrawwebsocket.infra.ws

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage
import com.toy.springrawwebsocket.infra.ws.dto.InboundFrame
import com.toy.springrawwebsocket.infra.ws.dto.MessageFrame
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class WireProtocol(
  private val jsonMapper: JsonMapper,
) {
  fun parse(text: String): InboundFrame? {
    val node = runCatching { jsonMapper.readTree(text) }.getOrNull() ?: return null
    val type = node.path("type").asString(null) ?: return null
    val roomId = node.path("roomId").asString(null)?.takeIf { it.isNotBlank() } ?: return null

    return InboundFrame(type, roomId, node.path("text").asString(null))
  }

  fun toMessageFrame(message: ChatMessage): String = jsonMapper.writeValueAsString(
    MessageFrame(
      id = requireNotNull(message.id),
      roomId = message.roomId,
      senderUserId = message.senderUserId,
      senderDeviceId = message.senderDeviceId,
      text = message.text,
      at = message.at.toEpochMilli(),
    ),
  )
}
