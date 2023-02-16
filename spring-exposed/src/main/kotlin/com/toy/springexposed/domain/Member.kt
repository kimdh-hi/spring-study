package com.toy.springexposed.domain

import org.jetbrains.exposed.dao.id.UUIDTable

object Member: UUIDTable(name = "member") {
  val name = varchar("name", 200)
    .uniqueIndex()

  val team = reference("team_id", Team)

  override fun toString(): String {
    return "Member(id=$id, name=$name)"
  }
}