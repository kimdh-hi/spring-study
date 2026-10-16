package com.toy.springsse

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
class SseEmitterRegistry {

  private val emitters = ConcurrentHashMap<String, SseEmitter>()
  private val eventId = AtomicLong()

  fun register(clientId: String): SseEmitter {
    val emitter = SseEmitter(TIMEOUT_MILLIS)

    emitter.onCompletion { emitters.remove(clientId, emitter) }
    emitter.onTimeout { emitter.complete() }
    emitter.onError { emitter.complete() }

    emitters[clientId] = emitter
    return emitter
  }

  fun send(clientId: String, name: String, data: Any) {
    emitters[clientId]?.send(event(name, data))
  }

  fun broadcast(name: String, data: Any) {
    emitters.values.forEach { runCatching { it.send(event(name, data)) } }
  }

  fun heartbeat() {
    emitters.values.forEach { runCatching { it.send(SseEmitter.event().comment("ping")) } }
  }

  private fun event(name: String, data: Any) =
    SseEmitter.event()
      .id(eventId.incrementAndGet().toString())
      .name(name)
      .data(data)
      .reconnectTime(RECONNECT_MILLIS)

  companion object {
    private const val TIMEOUT_MILLIS = 60 * 60 * 1000L
    private const val RECONNECT_MILLIS = 3_000L
  }
}
