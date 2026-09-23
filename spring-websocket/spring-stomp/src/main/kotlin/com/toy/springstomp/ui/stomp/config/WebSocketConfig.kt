package com.toy.springstomp.ui.stomp.config

import com.toy.springstomp.infra.stomp.error.StompErrorHandler
import com.toy.springstomp.ui.stomp.RoomSubscriptionInterceptor
import com.toy.springstomp.ui.stomp.SessionHandshakeInterceptor
import com.toy.springstomp.ui.stomp.SessionLifecycle
import com.toy.springstomp.ui.stomp.UserHandshakeHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.TaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

private const val CHAT_ENDPOINT = "/ws/chat"
private const val APP_PREFIX = "/app"
private const val TOPIC_PREFIX = "/topic"
private const val SEND_TIME_LIMIT_MILLIS = 10_000
private const val SEND_BUFFER_SIZE_BYTES = 512 * 1024
private const val HEARTBEAT_INTERVAL_MILLIS = 10_000L

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
  private val handshakeInterceptor: SessionHandshakeInterceptor,
  private val handshakeHandler: UserHandshakeHandler,
  private val sessionLifecycle: SessionLifecycle,
  private val roomSubscriptionInterceptor: RoomSubscriptionInterceptor,
  private val stompErrorHandler: StompErrorHandler,
  @Lazy @Qualifier("messageBrokerTaskScheduler") private val brokerTaskScheduler: TaskScheduler,
) : WebSocketMessageBrokerConfigurer {
  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.setErrorHandler(stompErrorHandler)
    registry
      .addEndpoint(CHAT_ENDPOINT)
      .addInterceptors(handshakeInterceptor)
      .setHandshakeHandler(handshakeHandler)
      .setAllowedOrigins("*")
  }

  override fun configureMessageBroker(registry: MessageBrokerRegistry) {
    registry
      .enableSimpleBroker(TOPIC_PREFIX)
      .setHeartbeatValue(longArrayOf(HEARTBEAT_INTERVAL_MILLIS, HEARTBEAT_INTERVAL_MILLIS))
      .setTaskScheduler(brokerTaskScheduler)
    registry.setApplicationDestinationPrefixes(APP_PREFIX)
  }

  override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
    registry
      .setSendTimeLimit(SEND_TIME_LIMIT_MILLIS)
      .setSendBufferSizeLimit(SEND_BUFFER_SIZE_BYTES)
      .addDecoratorFactory(sessionLifecycle)
  }

  override fun configureClientInboundChannel(registration: ChannelRegistration) {
    registration.interceptors(roomSubscriptionInterceptor)
  }
}
