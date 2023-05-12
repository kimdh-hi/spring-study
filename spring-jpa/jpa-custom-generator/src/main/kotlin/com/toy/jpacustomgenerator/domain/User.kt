package com.toy.jpacustomgenerator.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime

@Entity(name = "tb_user")
@Table
class User(
  @Id
  @GenericGenerator(name = "idGenerator", strategy = "com.toy.jpacustomgenerator.common.CustomIdGenerator")
  @GeneratedValue(generator = "idGenerator")
  var id: String = "",

  var name: String,

  var createdDate: LocalDateTime = LocalDateTime.now()
) {

  override fun toString(): String {
    return "User(id='$id', name='$name', createdDate=$createdDate)"
  }
}