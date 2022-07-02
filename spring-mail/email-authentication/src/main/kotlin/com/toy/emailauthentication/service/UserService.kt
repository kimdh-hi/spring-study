package com.toy.emailauthentication.service

import com.toy.emailauthentication.domain.User
import com.toy.emailauthentication.repository.UserRepository
import com.toy.emailauthentication.vo.SignupRequestVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  @Transactional
  fun signup(requestVO: SignupRequestVO): User {
    val user = User.newInstance(requestVO.username, requestVO.password)
    return userRepository.save(user)
  }
}