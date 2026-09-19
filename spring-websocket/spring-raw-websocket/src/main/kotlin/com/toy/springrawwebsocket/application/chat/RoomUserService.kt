package com.toy.springrawwebsocket.application.chat

import com.toy.springrawwebsocket.domain.chat.model.RoomUser
import com.toy.springrawwebsocket.domain.chat.repository.RoomUserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class RoomUserService(
  private val roomUserRepository: RoomUserRepository,
  private val eventPublisher: ApplicationEventPublisher,
) {
  @Transactional
  fun join(roomId: String, userId: String): Boolean {
    if (roomUserRepository.existsByRoomIdAndUserId(roomId, userId)) return false
    roomUserRepository.save(RoomUser(roomId, userId, Instant.now()))
    eventPublisher.publishEvent(RoomUserChangedEvent(userId, roomId, true))

    return true
  }

  @Transactional
  fun leave(roomId: String, userId: String): Boolean {
    if (roomUserRepository.deleteByRoomIdAndUserId(roomId, userId) == 0L) return false
    eventPublisher.publishEvent(RoomUserChangedEvent(userId, roomId, false))

    return true
  }

  @Transactional(readOnly = true)
  fun findRoomIds(userId: String): List<String> = roomUserRepository.findRoomIdsByUserId(userId)
}
