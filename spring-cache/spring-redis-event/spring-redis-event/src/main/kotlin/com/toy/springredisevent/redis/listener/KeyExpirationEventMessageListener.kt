package com.toy.springredisevent.redis.listener

import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class KeyExpirationEventMessageListener(
  private val redisMessageListenerContainer: RedisMessageListenerContainer
): KeyExpirationEventMessageListener(redisMessageListenerContainer) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doHandleMessage(message: Message) {
    log.info("doHandleMessage: {}", message)
  }
}