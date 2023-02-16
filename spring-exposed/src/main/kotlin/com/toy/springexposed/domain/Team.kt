package com.toy.springexposed.domain

import org.jetbrains.exposed.dao.id.UUIDTable

object Team: UUIDTable(name = "team") {
  val name = varchar("name", 200)
    .uniqueIndex()

  override fun toString(): String {
    return "Team(id=$id, name=$name)"
  }
}