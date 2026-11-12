package com.toy.springmfa.auth.passkey

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class PasskeyUser(
  @Id
  val id: String,

  @Column(nullable = false, unique = true)
  val username: String,

  @Column(nullable = false)
  val displayName: String,
)

interface PasskeyUserRepository : JpaRepository<PasskeyUser, String> {

  fun findByUsername(username: String): PasskeyUser?
}
