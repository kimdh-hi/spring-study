package com.toy.springexposed.domain

import org.jetbrains.exposed.dao.id.UUIDTable

object User: UUIDTable(name = "user") {
  val name = varchar("name", 200)
    .uniqueIndex()

  var status:

  override fun toString(): String {
    return "User(id=$id, name=$name)"
  }
}

enum class UserStatus {
  ENABLED, DISABLED
}