package com.toy.springstomp.support

import com.toy.springstomp.infra.stomp.error.ErrorResponse
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import java.lang.reflect.Type
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

private const val MESSAGE_HEADER = "message"
private val DEFAULT_TIMEOUT = Duration.ofSeconds(5)

class RecordingStompSessionHandler : StompSessionHandlerAdapter() {
  private val errors = LinkedBlockingQueue<ErrorFrame>()
  private val disconnectLatch = CountDownLatch(1)

  override fun getPayloadType(headers: StompHeaders): Type = ErrorResponse::class.java

  override fun handleFrame(headers: StompHeaders, payload: Any?) {
    errors.add(ErrorFrame(headers.getFirst(MESSAGE_HEADER).orEmpty(), payload as? ErrorResponse))
  }

  override fun handleTransportError(session: StompSession, exception: Throwable) {
    disconnectLatch.countDown()
  }

  fun awaitError(timeout: Duration = DEFAULT_TIMEOUT): ErrorFrame? =
    errors.poll(timeout.toMillis(), TimeUnit.MILLISECONDS)

  fun awaitDisconnect(timeout: Duration = DEFAULT_TIMEOUT): Boolean =
    disconnectLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS)

  data class ErrorFrame(val message: String, val response: ErrorResponse?)
}
