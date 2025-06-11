package com.toy.springevent.service

import com.toy.springevent.domain.User
import com.toy.springevent.domain.UserRepository
import com.toy.springevent.event.command.UserWelcomeMailSendCommand
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val eventPublisher: ApplicationEventPublisher,
) {

  @Transactional()
  fun signup(username: String, exception: Boolean = false, eventException: Boolean = false) {
    val user = userRepository.save(User.of(username))
    eventPublisher.publishEvent(UserWelcomeMailSendCommand(user.username, eventException))

    if (exception) {
      throw RuntimeException("ex...")
    }
  }
}
