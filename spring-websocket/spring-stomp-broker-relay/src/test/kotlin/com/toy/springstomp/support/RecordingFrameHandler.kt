package com.toy.springstomp.support

import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import java.lang.reflect.Type
import java.time.Duration
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

private val DEFAULT_TIMEOUT = Duration.ofSeconds(5)
private val SILENCE_TIMEOUT = Duration.ofSeconds(1)

class RecordingFrameHandler<T : Any>(
  private val payloadType: Class<T>,
) : StompFrameHandler {
  private val frames = LinkedBlockingQueue<T>()

  override fun getPayloadType(headers: StompHeaders): Type = payloadType

  override fun handleFrame(headers: StompHeaders, payload: Any?) {
    payload?.let { frames.add(payloadType.cast(it)) }
  }

  fun awaitFrame(timeout: Duration = DEFAULT_TIMEOUT): T? = frames.poll(timeout.toMillis(), TimeUnit.MILLISECONDS)

  fun hasNoFrame(timeout: Duration = SILENCE_TIMEOUT): Boolean =
    frames.poll(timeout.toMillis(), TimeUnit.MILLISECONDS) == null
}
