package com.toy.springmfa.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
  @Column(nullable = false, unique = true, length = 100)
  val username: String,

  @Column(nullable = false, length = 255)
  val password: String,

  @Column(nullable = false, length = 255)
  val roles: String,
) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
    protected set

  fun roleList(): List<String> = roles.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
