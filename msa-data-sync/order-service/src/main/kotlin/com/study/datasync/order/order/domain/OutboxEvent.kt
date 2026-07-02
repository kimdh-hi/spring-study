package com.study.datasync.order.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "outbox")
class OutboxEvent private constructor(
  @Id
  @UuidGenerator
  @Column(name = "id", length = 50)
  var id: String? = null,

  @Column(name = "aggregatetype", length = 100, nullable = false)
  var aggregateType: String,

  @Column(name = "aggregateid", length = 100, nullable = false)
  var aggregateId: String,

  @Column(name = "type", length = 100, nullable = false)
  var type: String,

  @Column(name = "payload", columnDefinition = "json", nullable = false)
  var payload: String,
) {
  companion object {
    fun of(aggregateType: String, aggregateId: String, type: String, payload: String) = OutboxEvent(
      aggregateType = aggregateType,
      aggregateId = aggregateId,
      type = type,
      payload = payload,
    )
  }
}
