package com.study.searchableencryption.message.domain.model

import com.study.searchableencryption.message.infra.EncryptedStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "chat_message", indexes = [Index(columnList = "room_id")])
class ChatMessage(
  @Column(name = "room_id", nullable = false)
  var roomId: Long,

  @Column(name = "sender_id", nullable = false)
  var senderId: Long,

  @Convert(converter = EncryptedStringConverter::class)
  @Column(nullable = false, length = 4000)
  var content: String,

  @Column(name = "sent_at", nullable = false)
  var sentAt: LocalDateTime = LocalDateTime.now(),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
)
