package com.toy.springredischat.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springredischat.vo.MessageVO
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisMessageListener(
  private val redisTemplate: RedisTemplate<String, Any>,
  private val objectMapper: ObjectMapper
): MessageListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onMessage(message: Message, pattern: ByteArray?) {
    val body = redisTemplate.stringSerializer.deserialize(message.body)
    val messageVO = objectMapper.readValue(body, MessageVO::class.java)
    log.info("receive message={}", messageVO)
  }
}