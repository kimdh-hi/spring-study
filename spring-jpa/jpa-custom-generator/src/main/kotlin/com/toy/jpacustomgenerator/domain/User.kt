package com.toy.jpacustomgenerator.domain

import com.toy.jpacustomgenerator.common.CustomIdGenerator
import com.toy.jpacustomgenerator.common.TimebasedUUID
import jakarta.persistence.Column
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
class User(
  @Id
  @GenericGenerator(name = "id", strategy = "com.toy.jpacustomgenerator.generator.CustomIdGenerator")
  var id: String = "",

  var name: String,

  var createdDate: LocalDateTime = LocalDateTime.now()
) {

  override fun toString(): String {
    return "User(id='$id', name='$name', createdDate=$createdDate)"
  }
}