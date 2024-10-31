package com.toy.springjpaexception.service

import com.toy.springjpaexception.domain.User
import com.toy.springjpaexception.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
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

  @Transactional
  fun save2(username: String, exception: Boolean): String {
    val user = userRepository.save(User.of(username))

    //transaction 안에서 runtimeException 을 잡는 것 자체를 하지 말자.
    runCatching { externalTransactionalService.logic2(exception) }

    return user.id!!
  }
}

@Service
class ExternalTransactionalService {

  @Transactional
  fun logic(exception: Boolean) {
    if (exception) throw RuntimeException("transactionalLogic error...") else return
  }

  //우회
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  fun logic2(exception: Boolean) {
    if (exception) throw RuntimeException("transactionalLogic error...") else return
  }
}
