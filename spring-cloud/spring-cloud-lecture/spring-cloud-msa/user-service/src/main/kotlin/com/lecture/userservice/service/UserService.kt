package com.lecture.userservice.service

import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.vo.UserSaveRequestVO
import com.lecture.userservice.vo.UserSaveResponseVO
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
  fun save(vo: UserSaveRequestVO): UserSaveResponseVO {
    val userEntity = vo.toEntity(passwordEncoder.encode(vo.password))
    return UserSaveResponseVO.of(userRepository.save(userEntity))
  }
}