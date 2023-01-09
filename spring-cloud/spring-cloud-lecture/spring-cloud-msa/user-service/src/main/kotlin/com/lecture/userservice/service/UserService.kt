package com.lecture.userservice.service

import com.lecture.userservice.domain.User
import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.vo.UserResponseVO
import com.lecture.userservice.vo.UserSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder
) {

  @Transactional
  fun save(vo: UserSaveRequestVO): UserResponseVO {
    val userEntity = vo.toEntity(passwordEncoder.encode(vo.password))
    return UserResponseVO.of(userRepository.save(userEntity))
  }

  fun findAll(): List<UserResponseVO> {
    return userRepository.findAll()
      .map { UserResponseVO.of(it) }
  }

  fun findById(id: Long): UserResponseVO {
    val user = userRepository.findByIdOrNull(id)
      ?: throw RuntimeException("user not found")
    return UserResponseVO.of(user)
  }
}