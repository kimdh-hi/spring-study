package com.toy.springexposed.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object User: LongIdTable(name = "user") {
  val userId = varchar("user_id", length = 200)
  val name = varchar("name", 200)

  override fun toString(): String {
    return "User(userId=$userId, name=$name)"
  }
}