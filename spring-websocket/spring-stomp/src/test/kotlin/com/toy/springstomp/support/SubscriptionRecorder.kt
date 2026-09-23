package com.toy.springstomp.support

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent
import java.util.concurrent.ConcurrentHashMap

/**
 * The server no longer stores subscriptions, so tests observe them through the STOMP session events
 * Spring publishes once a frame has passed the inbound channel.
 */
class SubscriptionRecorder {
  private val subscriptions = ConcurrentHashMap<String, Subscription>()

  @EventListener
  fun onSubscribe(event: SessionSubscribeEvent) {
    val accessor = StompHeaderAccessor.wrap(event.message)
    val destination = accessor.destination ?: return
    val userId = event.user?.name ?: return
    subscriptions[key(accessor)] = Subscription(userId, destination)
  }

  @EventListener
  fun onUnsubscribe(event: SessionUnsubscribeEvent) {
    subscriptions.remove(key(StompHeaderAccessor.wrap(event.message)))
  }

  @EventListener
  fun onDisconnect(event: SessionDisconnectEvent) {
    subscriptions.keys.removeIf { it.startsWith("${event.sessionId}|") }
  }

  fun isSubscribed(userId: String, destination: String): Boolean =
    subscriptions.values.any { it.userId == userId && it.destination == destination }

  fun clear() = subscriptions.clear()

  private fun key(accessor: StompHeaderAccessor): String = "${accessor.sessionId}|${accessor.subscriptionId}"

  private data class Subscription(val userId: String, val destination: String)
}

@TestConfiguration
class SubscriptionRecorderConfig {
  @Bean
  fun subscriptionRecorder(): SubscriptionRecorder = SubscriptionRecorder()
}
