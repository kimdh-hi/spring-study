package com.lecture.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
class User(
  @Id
  var id: Int,
  val name: String,
  val balance: Int,
)