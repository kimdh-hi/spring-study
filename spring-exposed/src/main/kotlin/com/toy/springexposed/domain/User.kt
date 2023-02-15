package com.toy.springexposed.domain

import org.jetbrains.exposed.dao.id.UUIDTable

object User: UUIDTable(name = "user") {
  val userId = uuid("user_id")
    .autoGenerate()

  val name = varchar("name", 200)
    .uniqueIndex()

  override fun toString(): String {
    return "User(userId=$userId, name=$name)"
  }
}