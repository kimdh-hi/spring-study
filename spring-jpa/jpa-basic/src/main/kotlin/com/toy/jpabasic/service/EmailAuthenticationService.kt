package com.toy.jpabasic.service

import com.toy.jpabasic.domain.EmailAuthentication
import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.EmailAuthenticationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EmailAuthenticationService(
  private val emailAuthenticationRepository: EmailAuthenticationRepository
) {

  @Transactional
  fun save(user: User) {
    val emailAuthentication = EmailAuthentication(user = user)
    emailAuthenticationRepository.save(emailAuthentication)
  }
}