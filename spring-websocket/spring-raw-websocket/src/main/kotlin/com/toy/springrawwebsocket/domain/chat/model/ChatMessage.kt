package com.toy.springrawwebsocket.domain.chat.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
  name = "chat_message",
  indexes = [Index(name = "idx_chat_message_room_id_id", columnList = "roomId, id")],
)
class ChatMessage(
  val roomId: String,
  val senderUserId: String,
  val senderDeviceId: String,
  @Column(length = 1000) val text: String,
  val at: Instant,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
}
