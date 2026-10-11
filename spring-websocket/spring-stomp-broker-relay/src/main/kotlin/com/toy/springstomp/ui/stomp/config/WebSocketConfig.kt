package com.toy.springstomp.ui.stomp.config

import com.toy.springstomp.infra.stomp.error.StompErrorHandler
import com.toy.springstomp.ui.stomp.RoomSubscriptionInterceptor
import com.toy.springstomp.ui.stomp.SessionHandshakeInterceptor
import com.toy.springstomp.ui.stomp.SessionLifecycleInterceptor
import com.toy.springstomp.ui.stomp.UserHandshakeHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.amqp.autoconfigure.RabbitProperties
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

private const val CHAT_ENDPOINT = "/ws/chat"
private const val APP_PREFIX = "/app"
private const val TOPIC_PREFIX = "/topic"

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
  private val handshakeInterceptor: SessionHandshakeInterceptor,
  private val handshakeHandler: UserHandshakeHandler,
  private val sessionLifecycleInterceptor: SessionLifecycleInterceptor,
  private val roomSubscriptionInterceptor: RoomSubscriptionInterceptor,
  private val stompErrorHandler: StompErrorHandler,
  private val rabbit: RabbitProperties,
  @Value("\${stomp.relay-port}") private val relayPort: Int,
) : WebSocketMessageBrokerConfigurer {
  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.setErrorHandler(stompErrorHandler)
    registry
      .addEndpoint(CHAT_ENDPOINT)
      .addInterceptors(handshakeInterceptor)
      .setHandshakeHandler(handshakeHandler)
  }

  override fun configureMessageBroker(registry: MessageBrokerRegistry) {
    registry
      .enableStompBrokerRelay(TOPIC_PREFIX)
      .setRelayHost(rabbit.determineHost())
      .setRelayPort(relayPort)
      .setVirtualHost(rabbit.determineVirtualHost() ?: "/")
      .setClientLogin(rabbit.determineUsername())
      .setClientPasscode(rabbit.determinePassword().orEmpty())
      .setSystemLogin(rabbit.determineUsername())
      .setSystemPasscode(rabbit.determinePassword().orEmpty())
    registry.setApplicationDestinationPrefixes(APP_PREFIX)
  }

  override fun configureClientInboundChannel(registration: ChannelRegistration) {
    registration.interceptors(sessionLifecycleInterceptor, roomSubscriptionInterceptor)
  }
}
