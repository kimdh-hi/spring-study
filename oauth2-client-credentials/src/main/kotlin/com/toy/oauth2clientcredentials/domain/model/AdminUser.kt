package com.toy.oauth2clientcredentials.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "admin_user")
class AdminUser(
  @Column(nullable = false, unique = true)
  val username: String,
  @Column(nullable = false)
  val password: String,
  @Id @GeneratedValue
  val id: Long? = null,
)
