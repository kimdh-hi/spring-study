package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User private constructor(
  @Column(length = 100, nullable = false)
  var name: String
) : UuidPrimaryKeyEntity() {

  companion object {
    operator fun invoke(name: String): User {
      return User(name = name)
    }
  }

  fun update(name: String) {
    this.name = name
  }

  override fun toString(): String {
    return "User(id=$id, name='$name')"
  }
}
