package com.toy.springjpaexception.service

import com.toy.springjpaexception.domain.User
import com.toy.springjpaexception.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val externalTransactionalService: ExternalTransactionalService,
) {

  @Transactional
  fun save(username: String, exception: Boolean): String {
    val user = userRepository.save(User.of(username))

    runCatching { externalTransactionalService.logic(exception) }

    return user.id!!
  }


}

@Service
@Transactional
class ExternalTransactionalService() {

  @Transactional
  fun logic(exception: Boolean) {
    if (exception) throw RuntimeException("transactionalLogic error...") else return
  }
}
