package com.toy.evenetmanagementapi.domain

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
abstract class AbstractEntity(
  @CreatedDate
  @Column(updatable = false)
  var createdAt: LocalDateTime? = null
)
