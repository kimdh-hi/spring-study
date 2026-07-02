package com.study.datasync.inventory.inventory.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "processed_event")
class ProcessedEvent(
  @Id
  @Column(length = 50)
  var eventId: String,
)
