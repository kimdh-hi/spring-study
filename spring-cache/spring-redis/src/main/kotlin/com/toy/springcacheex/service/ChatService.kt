package com.toy.springcacheex.service

import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class ChatService(
  private val container: RedisMessageListenerContainer,
  private val redisTemplate: StringRedisTemplate
): MessageListener {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onMessage(message: Message, pattern: ByteArray?) {
    log.info("onMessage message={}, pattern={}", message.toString(), pattern)
  }

  fun enter(roomName: String) {
    container.addMessageListener(this, ChannelTopic(roomName))

    val scanner = Scanner(System.`in`)
    while(scanner.hasNextLine()) {
      val line = scanner.nextLine()
      if(line == "q") {
        println("quit...")
        break
      }

      redisTemplate.convertAndSend(roomName, line)
    }

    container.removeMessageListener(this)
  }
}