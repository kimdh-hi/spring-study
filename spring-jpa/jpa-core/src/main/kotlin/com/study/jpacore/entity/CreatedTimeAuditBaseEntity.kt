package com.study.jpacore.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class CreatedTimeAuditBaseEntity {
  @Column(name = "created_date", updatable = false, nullable = false)
  @CreatedDate
  var createdDate: LocalDateTime = LocalDateTime.now()
}
