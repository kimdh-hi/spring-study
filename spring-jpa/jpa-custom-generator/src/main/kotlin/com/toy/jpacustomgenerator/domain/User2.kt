package com.toy.jpacustomgenerator.domain

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "tb_user2")
@EntityListeners(AuditingEntityListener::class)
class User2(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  var name: String,

  @CreatedDate
  var createdDate: LocalDateTime? = null
) {
  override fun toString(): String {
    return "User2(id='$id', name='$name', createdDate=$createdDate)"
  }
}