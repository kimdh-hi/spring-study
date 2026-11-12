package com.toy.springmfa.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
  @Column(nullable = false, unique = true)
  val username: String,

  @Column(nullable = false)
  val password: String,

  @Column(nullable = false)
  val roles: String,
) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
    protected set

  fun roleList(): List<String> = roles.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
