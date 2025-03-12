package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
  @Column(length = 100, nullable = false)
  var name: String
) : PkEntity() {
  fun update(name: String) {
    this.name = name
  }
}
