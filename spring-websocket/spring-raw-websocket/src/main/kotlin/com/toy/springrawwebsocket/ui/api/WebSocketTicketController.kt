package com.toy.springrawwebsocket.ui.api

import com.toy.springrawwebsocket.infra.redis.WebSocketTicketStore
import com.toy.springrawwebsocket.ui.api.constants.X_DEVICE_ID
import com.toy.springrawwebsocket.ui.api.constants.X_USER_ID
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class WebSocketTicketController(
  private val ticketStore: WebSocketTicketStore,
) {
  @PostMapping("/api/ws-tickets")
  fun issue(
    @RequestHeader(X_USER_ID) userId: String,
    @RequestHeader(X_DEVICE_ID) deviceId: String,
  ): Map<String, String> = mapOf("ticket" to ticketStore.issue(userId, deviceId))
}
