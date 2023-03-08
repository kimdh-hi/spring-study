package com.toy.springredisevent.redis.listener

import com.toy.springredisevent.user.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.repository.findByIdOrNull

//@Component
class RedisExpireEventListener(
  private val redisMessageListenerContainer: RedisMessageListenerContainer,
  private val userRepository: UserRepository
): KeyExpirationEventMessageListener(redisMessageListenerContainer) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doHandleMessage(message: Message) {
    var expiredKey = String(message.body)
    log.info("KeyExpirationEventMessageListener.doHandleMessage: {}", expiredKey)

    expiredKey = expiredKey.split(":")[1]
    userRepository.findByIdOrNull(expiredKey)?.let {
      log.info("mqtt 메시지 발행")
      it.initStatus()
      userRepository.save(it)
    }
  }
}