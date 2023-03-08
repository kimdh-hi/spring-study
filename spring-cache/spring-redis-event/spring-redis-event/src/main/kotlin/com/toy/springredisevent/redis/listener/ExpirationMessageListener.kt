package com.toy.springredisevent.redis.listener

import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ExpirationListener : MessageListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onMessage(message: Message, pattern: ByteArray?) {
    log.info("ExpirationListener.onMessage expired-key: {}", message)
  }
}