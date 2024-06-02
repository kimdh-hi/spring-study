package com.toy.jpabulkinsert.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @UuidGenerator
  var id: String? = null,

  var name: String
) {
  override fun toString(): String {
    return "User(id=$id, name='$name')"
  }
}