package com.toy.restdocsdemo.service

import com.toy.restdocsdemo.repository.UserRepository
import com.toy.restdocsdemo.vo.UserCreateRequestVO
import com.toy.restdocsdemo.vo.UserResponseVO
import com.toy.restdocsdemo.vo.UserUpdateRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  @Transactional
  fun save(requestVO: UserCreateRequestVO): UserResponseVO {
    val user = requestVO.toEntity()
    val savedUser = userRepository.save(user)
    return UserResponseVO.of(savedUser)
  }

  fun list(): List<UserResponseVO> {
    return userRepository.findAll()
      .map { UserResponseVO.of(it) }
  }

  fun get(id: Long): UserResponseVO {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found ...")
    return UserResponseVO.of(user)
  }

  @Transactional
  fun update(id: Long, requestVO: UserUpdateRequestVO) {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found ...")
    user.update(requestVO.name)
  }
}