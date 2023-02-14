package com.toy.springmongo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("user")
class User(
  @Id
  var id: String? = null,
  var name: String,
) {

  fun update(name: String) {
    this.name = name
  }
}