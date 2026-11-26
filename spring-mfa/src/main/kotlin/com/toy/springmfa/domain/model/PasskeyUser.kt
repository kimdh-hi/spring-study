package com.toy.springmfa.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class PasskeyUser(
  @Id
  @Column(nullable = false, length = 86)
  val id: String,

  @Column(nullable = false, unique = true, length = 100)
  val username: String,

  @Column(nullable = false, length = 100)
  val displayName: String,
)
