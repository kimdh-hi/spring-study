package com.toy.springrawwebsocket.domain.chat.repository

import com.toy.springrawwebsocket.domain.chat.model.RoomUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RoomUserRepository : JpaRepository<RoomUser, Long> {
  fun existsByRoomIdAndUserId(roomId: String, userId: String): Boolean

  fun deleteByRoomIdAndUserId(roomId: String, userId: String): Long

  @Query("select roomUser.userId from RoomUser roomUser where roomUser.roomId = :roomId")
  fun findUserIdsByRoomId(roomId: String): List<String>

  @Query("select roomUser.roomId from RoomUser roomUser where roomUser.userId = :userId")
  fun findRoomIdsByUserId(userId: String): List<String>
}
