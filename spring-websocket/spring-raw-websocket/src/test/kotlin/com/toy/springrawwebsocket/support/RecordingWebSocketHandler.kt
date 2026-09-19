package com.toy.springrawwebsocket.support

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

private val DEFAULT_TIMEOUT = Duration.ofSeconds(5)

class RecordingWebSocketHandler(
  private val jsonMapper: JsonMapper,
) : TextWebSocketHandler() {
  private val frames = LinkedBlockingQueue<String>()
  private val closeLatch = CountDownLatch(1)

  @Volatile
  var closeStatus: CloseStatus? = null
    private set

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    frames.add(message.payload)
  }

  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    closeStatus = status
    closeLatch.countDown()
  }

  fun awaitFrame(type: String, timeout: Duration = DEFAULT_TIMEOUT): JsonNode? {
    val deadline = System.nanoTime() + timeout.toNanos()
    while (System.nanoTime() < deadline) {
      val remaining = deadline - System.nanoTime()
      val payload = frames.poll(remaining, TimeUnit.NANOSECONDS) ?: return null
      val node = jsonMapper.readTree(payload)
      if (node.path("type").asString(null) == type) return node
    }

    return null
  }

  fun awaitClose(timeout: Duration = DEFAULT_TIMEOUT): Boolean = closeLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS)

  fun hasNoFrame(timeout: Duration = Duration.ofSeconds(1)): Boolean =
    frames.poll(timeout.toMillis(), TimeUnit.MILLISECONDS) == null
}
