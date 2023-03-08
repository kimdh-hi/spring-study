package com.toy.springredisevent.user.service

import com.toy.springredisevent.controller.UserStatusUpdateRequestVO
import com.toy.springredisevent.user.domain.User
import com.toy.springredisevent.user.domain.UserRepository
import com.toy.springredisevent.user.domain.UserStatusHash
import com.toy.springredisevent.user.domain.UserStatusHashRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val userStatusHashRepository: UserStatusHashRepository,
  private val userStatusCacheHelper: UserStatusCacheHelper
) {

  @Transactional
  fun updateStatus(userId: String, vo: UserStatusUpdateRequestVO) {
    val user = userRepository.findByIdOrNull(userId) ?: throw RuntimeException("user not found")

    val userStatus = vo.toUserStatus()
    user.updateStatus(userStatus)

    createUserStatusHash(user, vo.ttlSeconds)
  }

  private fun createUserStatusHash(user: User, ttlSeconds: Long) {
    val userStatusHash = UserStatusHash(user.id!!, user.status!!.statusName!!, ttlSeconds)
    userStatusHashRepository.save(userStatusHash)
  }

  @Transactional
  fun updateStatusV2(userId: String, vo: UserStatusUpdateRequestVO) {
    val user = userRepository.findByIdOrNull(userId) ?: throw RuntimeException("user not found")

    val userStatus = vo.toUserStatus()
    user.updateStatus(userStatus)

    userStatusCacheHelper.put(userId, userStatus, Duration.ofSeconds(vo.ttlSeconds))
  }
}