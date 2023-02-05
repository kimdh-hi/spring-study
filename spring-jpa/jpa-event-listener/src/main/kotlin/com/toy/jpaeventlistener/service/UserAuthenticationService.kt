package com.toy.jpaeventlistener.service

import com.toy.jpaeventlistener.domain.User
import com.toy.jpaeventlistener.domain.UserAuthenticationEmail
import com.toy.jpaeventlistener.domain.UserAuthenticationEmailRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserAuthenticationService(
  private val userAuthenticationEmailRepository: UserAuthenticationEmailRepository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun save(user: User) {
    log.info("UserAuthenticationService.save {}", user)

    val userAuthenticationEmail = UserAuthenticationEmail(user = user)
    userAuthenticationEmailRepository.save(userAuthenticationEmail)
  }
}