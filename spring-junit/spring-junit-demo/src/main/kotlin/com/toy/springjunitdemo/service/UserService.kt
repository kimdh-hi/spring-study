package com.toy.springjunitdemo.service

import com.toy.springjunitdemo.controller.UserRequestVO
import com.toy.springjunitdemo.controller.UserResponseVO
import com.toy.springjunitdemo.domain.User
import com.toy.springjunitdemo.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

//@Transactional(readOnly = true)
@Service
class UserService(
  private val userRepository: UserRepository,
  private val mailSender: MailSender
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun list() = userRepository.findAll()

  fun get(id: Long) = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")

  @Transactional
  fun save(requestVO: UserRequestVO) = userRepository.save(requestVO.toEntity())

  @Transactional
  fun update(id: Long, requestVO: UserRequestVO): UserResponseVO {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")
    user.username = requestVO.username
    return UserResponseVO.of(user)
  }

  @Transactional
  fun delete(id: Long) {
    userRepository.deleteById(id)
    mailSender.sendNotification()
  }

}