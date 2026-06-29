package com.study.readwritesplitting.application

import com.study.readwritesplitting.domain.user.User
import com.study.readwritesplitting.domain.user.repository.UserRepository
import com.study.readwritesplitting.domain.user.repository.WhereAmI
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional
  fun save(name: String): String {
    val user = userRepository.save(User.of(name))
    return user.id!!
  }

  @Transactional(readOnly = true)
  fun whereAmIOnRead(): WhereAmI = userRepository.whereAmI()

  @Transactional
  fun whereAmIOnWrite(): WhereAmI = userRepository.whereAmI()

  @Transactional(readOnly = true)
  fun count(): Long = userRepository.count()
}
