package com.study.readwritesplitting.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.EntityListeners
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
  @CreatedDate
  @Column(updatable = false)
  var createdAt: LocalDateTime? = null
}
