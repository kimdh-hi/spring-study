package com.toy.reactivejdsl.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import kotlin.reflect.typeOf

abstract class BaseEntity: Serializable{
  companion object {
    @Serial
    private const val serialVersionUID: Long = -5774120770574498000L
  }

  abstract fun getPk(): Any?
  abstract fun getType(): Any?
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractDateTraceEntity(
  @Column(name = "created_date", updatable = false)
  @CreatedDate
  var createdDate: LocalDateTime? = null,

  @Column(name = "updated_date")
  @CreatedDate
  var updatedDate: LocalDateTime? = null
): BaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 8525985060732747902L
  }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractByTraceEntity(
  @Column(name = "created_by", updatable = false)
  @CreatedBy
  var createdBy: String? = null,

  @Column(name = "updated_by")
  @LastModifiedBy
  var updatedBy: String? = null
): AbstractDateTraceEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 8525985060732747902L
  }
}