package com.toy.springrawwebsocket.ui.ws.config

import com.toy.springrawwebsocket.ui.ws.SessionHandshakeInterceptor
import com.toy.springrawwebsocket.ui.ws.WebSocketMessageHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
  private val messageHandler: WebSocketMessageHandler,
  private val handshakeInterceptor: SessionHandshakeInterceptor,
) : WebSocketConfigurer {
  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
      .addHandler(messageHandler, "/ws/chat")
      .addInterceptors(handshakeInterceptor)
      .setAllowedOrigins("*")
  }
}
