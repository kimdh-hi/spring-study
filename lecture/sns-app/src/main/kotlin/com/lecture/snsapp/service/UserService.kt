package com.lecture.snsapp.service

import com.lecture.snsapp.domain.User
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.AlarmRepository
import com.lecture.snsapp.repository.UserRepository
import com.lecture.snsapp.vo.UserResponseVO
import com.lecture.snsapp.utils.JwtUtils
import com.lecture.snsapp.vo.AlarmResponseVO
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
  private val alarmRepository: AlarmRepository,
  private val passwordEncoder: PasswordEncoder
) {

  @Value("\${jwt.secret-key}") lateinit var secretKey: String
  @Value("\${jwt.expiry-time-ms}") lateinit var expiryTimeMs: String

  @Transactional
  fun join(username: String, password: String): UserResponseVO {
    userRepository.findByUsername(username)?.let {
      throw ApplicationException(ErrorCode.DUPLICATED_USER_NAME)
    }
    val user = User.of(username = username, password = passwordEncoder.encode(password))
    val savedUser = userRepository.save(user)
    return UserResponseVO.of(savedUser)
  }

  fun login(username: String, password: String): String {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.LOGIN_FAILED)
    if(!passwordEncoder.matches(password, user.password))
      throw ApplicationException(ErrorCode.LOGIN_FAILED)
    return JwtUtils.generateToken(username, secretKey, expiryTimeMs.toLong())
  }

  fun loadUserByUsername(username: String): User
    = userRepository.findByUsername(username) ?: throw  ApplicationException(ErrorCode.USER_NOT_FOUND)

  fun getAlarmList(userId: String, pageable: Pageable): Page<AlarmResponseVO> {
    return alarmRepository.findAllByUserId(userId, pageable)
      .map { AlarmResponseVO.of(it) }

  }
}