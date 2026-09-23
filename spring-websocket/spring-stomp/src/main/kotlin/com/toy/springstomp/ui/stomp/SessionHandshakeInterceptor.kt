package com.toy.springstomp.ui.stomp

import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.util.UriComponentsBuilder

const val WS_ATTR_CHAT_USER = "chat.user"

@Component
class SessionHandshakeInterceptor : HandshakeInterceptor {
  override fun beforeHandshake(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    wsHandler: WebSocketHandler,
    attributes: MutableMap<String, Any>,
  ): Boolean {
    val params = UriComponentsBuilder.fromUri(request.uri).build().queryParams
    val userId = params.getFirst("userId")?.takeIf { it.isNotBlank() } ?: return reject(response)
    val deviceId = params.getFirst("deviceId")?.takeIf { it.isNotBlank() } ?: return reject(response)
    attributes[WS_ATTR_CHAT_USER] = ChatUser(userId, deviceId)

    return true
  }

  override fun afterHandshake(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    wsHandler: WebSocketHandler,
    exception: Exception?,
  ) = Unit

  private fun reject(response: ServerHttpResponse): Boolean {
    response.setStatusCode(HttpStatus.UNAUTHORIZED)

    return false
  }
}
