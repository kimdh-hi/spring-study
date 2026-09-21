package com.toy.springrawwebsocket.infra.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class RedisPublisher(
  private val redisTemplate: StringRedisTemplate,
  private val jsonMapper: JsonMapper,
) {
  fun publish(channel: String, message: Any): Long = publishRaw(channel, jsonMapper.writeValueAsString(message))

  fun publishRaw(channel: String, payload: String): Long = redisTemplate.convertAndSend(channel, payload) ?: 0L
}
