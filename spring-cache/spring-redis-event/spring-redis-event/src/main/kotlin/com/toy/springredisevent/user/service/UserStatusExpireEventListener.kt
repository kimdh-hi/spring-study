package com.toy.springredisevent.user.service

import com.toy.springredisevent.common.CacheConstants
import com.toy.springredisevent.user.domain.UserRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.core.RedisKeyExpiredEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

// KeyExpirationEventMessageListener 의 doHandleMessage 가 종료되고 publishEvent 에 의해 발행되는 이벤트 수신
// RedisHash CrudRepository 를 사용하는 경우 save 시 사본을 함께 저장한다. (key:phantom)
@Component
class UserStatusExpireEventListener(
  private val userRepository: UserRepository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @EventListener
  fun eventHandler(event: RedisKeyExpiredEvent<*>) {
    log.info("UserStatusExpireEventListener.eventHandler: {}", String(event.id))
    var userId = String(event.id)
    if (!StringUtils.contains(userId, CacheConstants.USER_STATUS)) {
      return
    }

    userId = StringUtils.substringAfterLast(userId, CacheKeyPrefix.SEPARATOR)
    userRepository.findByIdOrNull(userId)?.let {
      it.initStatus()
      userRepository.save(it)

      log.info("mqtt 메시지 발행")
    }
  }
}