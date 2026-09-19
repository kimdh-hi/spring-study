package com.toy.springrawwebsocket.infra.ws

import com.toy.springrawwebsocket.infra.ws.constants.PONG_TIMED_OUT
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.socket.PingMessage
import java.time.Duration
import java.time.Instant

private const val PING_INTERVAL = "PT30S"
private val PONG_TIMEOUT = Duration.ofSeconds(90)

@Component
class ConnectionHeartbeat(
  private val localSessionRegistry: SessionRegistry,
  private val sessionSender: SessionSender,
) {
  @Scheduled(fixedDelayString = PING_INTERVAL)
  fun ping() {
    val now = Instant.now()
    localSessionRegistry.findEntries().forEach { entry ->
      when {
        entry.isStale(now, PONG_TIMEOUT) -> sessionSender.close(entry, PONG_TIMED_OUT)
        else -> sessionSender.send(entry, PingMessage())
      }
    }
  }
}
