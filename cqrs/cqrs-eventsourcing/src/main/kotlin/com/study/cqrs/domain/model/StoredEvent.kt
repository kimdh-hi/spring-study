package com.study.cqrs.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class StoredEvent(
  val aggregateId: String,
  val sequence: Int,
  val type: String,
  @Column(columnDefinition = "text")
  val payload: String,
  val occurredAt: LocalDateTime,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0
}
