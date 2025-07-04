package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseCreateTimestampEntity : UuidPrimaryKeyEntity() {
  @Column(name = "created_at", updatable = false, nullable = false)
  @CreatedDate
  private var _createdAt: Instant? = null

  val createdAt: Instant
    get() = _createdAt ?: throw IllegalStateException("createdAt is null.")

  override fun isNew(): Boolean = _createdAt == null
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTraceableEntity : BaseCreateTimestampEntity() {
  @Column(name = "updated_date")
  @LastModifiedDate
  var updatedAt: Instant? = null

  @Column(name = "updated_by", length = 50)
  @LastModifiedBy
  var updatedBy: String? = null
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseCreateTimestampWithoutIdEntity {
  @Column(updatable = false)
  @CreatedDate
  protected var createdAt: Instant? = null
}
