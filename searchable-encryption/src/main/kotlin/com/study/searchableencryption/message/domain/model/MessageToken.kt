package com.study.searchableencryption.message.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "chat_message_token", indexes = [Index(columnList = "token")])
class MessageToken(
  @Column(name = "message_id", nullable = false)
  var messageId: Long,

  @Column(nullable = false, length = 32)
  var token: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
)
