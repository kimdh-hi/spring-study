package com.toy.springstomp.application.chat

import com.toy.springstomp.domain.chat.model.RoomUser
import com.toy.springstomp.domain.chat.repository.RoomUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class RoomUserService(
  private val roomUserRepository: RoomUserRepository,
) {
  @Transactional
  fun join(roomId: String, userId: String): Boolean {
    if (isMember(roomId, userId)) return false
    roomUserRepository.save(RoomUser(roomId, userId, Instant.now()))

    return true
  }

  @Transactional
  fun leave(roomId: String, userId: String): Boolean = roomUserRepository.deleteByRoomIdAndUserId(roomId, userId) > 0L

  @Transactional(readOnly = true)
  fun findRoomIds(userId: String): List<String> = roomUserRepository.findRoomIdsByUserId(userId)

  @Transactional(readOnly = true)
  fun isMember(roomId: String, userId: String): Boolean = roomUserRepository.existsByRoomIdAndUserId(roomId, userId)
}
