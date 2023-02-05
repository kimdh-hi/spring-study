package com.toy.jpaeventlistener.service

import com.toy.jpaeventlistener.domain.User
import com.toy.jpaeventlistener.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val eventPublisher: ApplicationEventPublisher
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  fun save(@RequestBody requestVO: UserSaveRequestVO): User {
    val entity = requestVO.toEntity()
    val user = userRepository.saveAndFlush(entity)

    eventPublisher.publishEvent(UserAuthenticationEmailSendEvent(user))

    return user
  }

  fun saveEventSend(requestVO: UserSaveRequestVO) {
    val entity = requestVO.toEntity()
    eventPublisher.publishEvent(UserSaveEvent(entity))
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun saveByEvent(user: User): User {
    log.info("saveByEvent user={}", user)
    return userRepository.save(user)
  }
}

data class UserSaveRequestVO(
  val username: String
) {
  fun toEntity() = User(
    username = username
  )
}

data class UserAuthenticationEmailSendEvent(
  val user: User
)

data class UserSaveEvent(
  val user: User
)