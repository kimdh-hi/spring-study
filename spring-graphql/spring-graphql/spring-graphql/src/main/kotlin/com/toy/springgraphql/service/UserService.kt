package com.toy.springgraphql.service

import com.toy.springgraphql.domain.User
import com.toy.springgraphql.repository.UserRepository
import com.toy.springgraphql.vo.UserResponseVO
import com.toy.springgraphql.vo.UserSaveRequestVO

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  @Transactional
  fun save(requestVO: UserSaveRequestVO): UserResponseVO {
    val user = User.of(requestVO.name, requestVO.username)
    return UserResponseVO.of(userRepository.save(user))
  }

  fun find(id: String): UserResponseVO {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")
    return UserResponseVO.of(user)
  }

  fun findAll(): List<UserResponseVO> {
    return userRepository.findAll()
      .map { UserResponseVO.of(it) }
  }
}