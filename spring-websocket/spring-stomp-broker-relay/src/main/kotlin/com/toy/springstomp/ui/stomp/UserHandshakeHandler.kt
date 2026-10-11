package com.toy.springstomp.ui.stomp

import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class UserHandshakeHandler : DefaultHandshakeHandler() {
  override fun determineUser(
    request: ServerHttpRequest,
    wsHandler: WebSocketHandler,
    attributes: MutableMap<String, Any>,
  ): Principal = attributes.getValue(WS_ATTR_CHAT_USER) as ChatUser
}
