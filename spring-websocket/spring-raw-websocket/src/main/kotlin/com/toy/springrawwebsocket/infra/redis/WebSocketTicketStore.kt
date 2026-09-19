package com.toy.springrawwebsocket.infra.redis

import com.toy.springrawwebsocket.infra.redis.dto.WebSocketTicket
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import java.time.Duration
import java.util.UUID

private const val TICKET_KEY_PREFIX = "ws:ticket:"
private val TICKET_TTL = Duration.ofSeconds(60)

@Component
class WebSocketTicketStore(
  private val redisTemplate: StringRedisTemplate,
  private val jsonMapper: JsonMapper,
) {
  fun issue(userId: String, deviceId: String): String {
    val ticket = UUID.randomUUID().toString()
    val payload = jsonMapper.writeValueAsString(WebSocketTicket(userId, deviceId))
    redisTemplate.opsForValue().set(TICKET_KEY_PREFIX + ticket, payload, TICKET_TTL)

    return ticket
  }

  fun consume(ticket: String): WebSocketTicket? {
    val payload = redisTemplate.opsForValue().getAndDelete(TICKET_KEY_PREFIX + ticket) ?: return null

    return runCatching { jsonMapper.readValue(payload, WebSocketTicket::class.java) }.getOrNull()
  }
}
