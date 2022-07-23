package com.toy.productinfo.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractDateTraceEntity (

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  var createdDate: LocalDateTime? = null,

  @LastModifiedDate
  @Column(name = "updated_date")
  var updatedDate: LocalDateTime? = null
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2826063408310672537L
  }
}

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractByTraceEntity (

  @CreatedBy
  @Column(updatable = false, name = "created_by")
  var createdBy: String? = null,

  @LastModifiedBy
  @Column(name = "updated_by")
  var updatedBy: String? = null,
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -3105821315542469349L
  }

}

abstract class  AbstractBaseEntity: Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 7660379920119327436L
  }
}


