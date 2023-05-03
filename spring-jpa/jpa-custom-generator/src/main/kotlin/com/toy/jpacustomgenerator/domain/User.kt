package com.toy.jpacustomgenerator.domain

import com.toy.jpacustomgenerator.common.CustomIdGenerator
import com.toy.jpacustomgenerator.common.TimebasedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity(name = "tb_user")
@Table
@EntityListeners(AuditingEntityListener::class)
class User(
  @Id
  @GeneratedValue
  @TimebasedId(CustomIdGenerator::class)
  var id: String = "",

  var name: String,

  @CreatedDate
  var createdDate: LocalDateTime? = null
) {
  override fun toString(): String {
    return "User(id='$id', name='$name', createdDate=$createdDate)"
  }
}