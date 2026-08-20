package com.study.springoidc.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
  @Column(nullable = false, unique = true)
  val username: String,

  @Column(nullable = false)
  val password: String,

  @Column(nullable = false)
  val email: String,

  @Column(nullable = false)
  val nickname: String,

  @Column(nullable = false)
  val roles: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
) {
  val roleList: List<String> get() = roles.split(",").map { it.trim() }
}
