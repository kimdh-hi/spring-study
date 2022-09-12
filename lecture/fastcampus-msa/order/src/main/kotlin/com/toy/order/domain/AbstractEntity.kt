package com.toy.order.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractEntity {
  @CreationTimestamp
  val createdAt: ZonedDateTime? = null

  @UpdateTimestamp
  val updatedAt: ZonedDateTime? = null
}