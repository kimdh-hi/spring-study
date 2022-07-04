package com.toy.webfluxr2dbcpostgres.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "tb_user")
class User (
  @field:Id
  var id: Long? = null,
  var name: String,
  var username: String,
  var password: String
) {
  companion object {
    fun newUser(name: String, username: String, password: String): User {
      return User(
        name = name, username = username, password = password
      )
    }
  }

  fun update(
    name: String?, username: String?, password: String?
  ) {
    name?.let { this.name = it }
    username?.let { this.username = it }
    password?.let { this.password = it }
  }
}