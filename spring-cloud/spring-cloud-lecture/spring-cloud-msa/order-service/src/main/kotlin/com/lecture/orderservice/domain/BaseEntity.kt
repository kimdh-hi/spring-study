package com.lecture.orderservice.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractBaseEntity: Serializable {

  @Column(name = "created_date", updatable = false)
  @CreatedDate
  var createdDate: LocalDateTime? = null

  @Column(name = "updated_date")
  @LastModifiedDate
  var updatedDate: LocalDateTime? = null

  companion object {
    @Serial
    private const val serialVersionUID: Long = -4103160044841166190L
  }
}