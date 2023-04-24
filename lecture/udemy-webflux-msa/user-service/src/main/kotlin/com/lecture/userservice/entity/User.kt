package com.lecture.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
class User(
  @Id
  var id: Int,
  var name: String,
  var balance: Int,
) {

  fun update(name: String?, balance: Int?): User {
    name?.let { this.name = it }
    balance?.let { this.balance = it }
    return this
  }
}