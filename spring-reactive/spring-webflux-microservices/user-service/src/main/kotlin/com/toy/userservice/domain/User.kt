package com.toy.userservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tb_users")
data class User(
  @Id
  var id: Int? = null,
  val name: String,
  val balance: Int
)