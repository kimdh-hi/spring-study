package com.toy.springrawwebsocket.ui.ws

import com.toy.springrawwebsocket.infra.redis.WebSocketTicketStore
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_DEVICE_ID
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_SESSION_ID
import com.toy.springrawwebsocket.ui.ws.constants.WS_ATTR_USER_ID
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID

@Component
class SessionHandshakeInterceptor(
  private val ticketStore: WebSocketTicketStore,
) : HandshakeInterceptor {
  override fun beforeHandshake(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    wsHandler: WebSocketHandler,
    attributes: MutableMap<String, Any>,
  ): Boolean {
    val params = UriComponentsBuilder.fromUri(request.uri).build().queryParams
    val ticket = params.getFirst("ticket")?.takeIf { it.isNotBlank() } ?: return reject(response)
    val consumed = ticketStore.consume(ticket) ?: return reject(response)
    attributes[WS_ATTR_USER_ID] = consumed.userId
    attributes[WS_ATTR_DEVICE_ID] = consumed.deviceId
    attributes[WS_ATTR_SESSION_ID] = UUID.randomUUID().toString()

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
