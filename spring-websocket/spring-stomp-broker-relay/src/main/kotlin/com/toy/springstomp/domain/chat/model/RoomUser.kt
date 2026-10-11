package com.toy.springstomp.domain.chat.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant

@Entity
@Table(
  name = "room_user",
  uniqueConstraints = [UniqueConstraint(name = "uk_room_user", columnNames = ["roomId", "userId"])],
  indexes = [Index(name = "idx_room_user_user_id", columnList = "userId")],
)
class RoomUser(
  val roomId: String,
  val userId: String,
  val joinedAt: Instant,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
}
