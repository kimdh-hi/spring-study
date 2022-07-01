package com.toy.noticeservice.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractBaseEntity: Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -7059692734974747637L
  }

}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractTraceDateEntity (
  var createdDate: LocalDateTime? = null,
  var updatedDate: LocalDateTime? = null,
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 3089665087205020423L
  }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractTraceByEntity(
  var createdBy: String? = null,
  var updatedBy: String? = null
): AbstractTraceDateEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 3415911391359543591L
  }
}