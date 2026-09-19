package com.toy.springrawwebsocket.ui.ws

import com.toy.springrawwebsocket.application.chat.ChatService
import com.toy.springrawwebsocket.infra.redis.RedisChannels
import com.toy.springrawwebsocket.infra.redis.RedisPublisher
import com.toy.springrawwebsocket.infra.redis.dto.DuplicateSessionMessage
import com.toy.springrawwebsocket.infra.ws.RoomSubscriptions
import com.toy.springrawwebsocket.infra.ws.SessionEntry
import com.toy.springrawwebsocket.infra.ws.SessionRegistry
import com.toy.springrawwebsocket.infra.ws.SessionSender
import com.toy.springrawwebsocket.infra.ws.WireProtocol
import com.toy.springrawwebsocket.infra.ws.constants.DUPLICATE_SESSION
import com.toy.springrawwebsocket.infra.ws.constants.FrameTypes
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_DEVICE_ID
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_SESSION_ID
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_USER_ID
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.Instant

private const val SEND_TIME_LIMIT_MILLIS = 10_000
private const val SEND_BUFFER_SIZE_BYTES = 512 * 1024

@Component
class WebSocketMessageHandler(
  private val localSessionRegistry: SessionRegistry,
  private val roomSubscriptions: RoomSubscriptions,
  private val sessionSender: SessionSender,
  private val publisher: RedisPublisher,
  private val chatService: ChatService,
  private val wireProtocol: WireProtocol,
) : TextWebSocketHandler() {
  override fun afterConnectionEstablished(session: WebSocketSession) {
    val concurrentSession = ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT_MILLIS, SEND_BUFFER_SIZE_BYTES)
    val entry = SessionEntry(
      session = concurrentSession,
      sessionId = session.sessionId(),
      deviceId = session.deviceId(),
      userId = session.userId(),
      connectedAt = Instant.now(),
    )
    localSessionRegistry.register(entry)?.let { sessionSender.close(it, DUPLICATE_SESSION) }
    publisher.publish(RedisChannels.DUPLICATE_SESSION, DuplicateSessionMessage(entry.deviceId, entry.sessionId))
    roomSubscriptions.attach(entry)
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    val entry = localSessionRegistry.findEntryBySessionId(session.sessionId()) ?: return
    val frame = wireProtocol.parse(message.payload) ?: return
    when (frame.type) {
      FrameTypes.SEND -> chatService.send(entry.toDeviceSession(), frame.roomId, frame.text.orEmpty())
    }
  }

  override fun handlePongMessage(session: WebSocketSession, message: PongMessage) {
    localSessionRegistry.findEntryBySessionId(session.sessionId())?.markPong(Instant.now())
  }

  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    roomSubscriptions.detach(session.sessionId())
  }

  private fun WebSocketSession.sessionId(): String = attributes[WS_ATTR_SESSION_ID] as String

  private fun WebSocketSession.deviceId(): String = attributes[WS_ATTR_DEVICE_ID] as String

  private fun WebSocketSession.userId(): String = attributes[WS_ATTR_USER_ID] as String
}
