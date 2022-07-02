package com.toy.coroutinevsreactive.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "tb_user")
data class User (

  @Id
  var id: Long? = null,

  var username: String,
)
