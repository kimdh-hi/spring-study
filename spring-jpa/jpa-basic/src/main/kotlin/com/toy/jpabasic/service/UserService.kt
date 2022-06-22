package com.toy.jpabasic.service

import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.EmailAuthenticationRepository
import com.toy.jpabasic.repository.UserRepository
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val emailAuthenticationService: EmailAuthenticationService
) {

  @Transactional(rollbackFor = [IllegalArgumentException::class])
  fun save(requestVO: UserSaveRequestVO): User {
    val user = requestVO.toEntity()
    val savedUser = userRepository.save(user)
    emailAuthenticationService.save(savedUser)
    if (savedUser.username == "error") {
      throw IllegalArgumentException("error ...")
    }

    return savedUser
  }
}