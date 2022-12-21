package com.toy.springwebsocket.config

import com.toy.springwebsocket.chat.ChatHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@EnableWebSocket
@Configuration
class WebSocketConfig(
  private val chatHandler: ChatHandler
): WebSocketConfigurer {

  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
      .addHandler(chatHandler, "/ws/chat")
      .setAllowedOrigins("*")
  }
}