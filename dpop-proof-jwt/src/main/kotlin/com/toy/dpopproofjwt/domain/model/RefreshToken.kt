package com.toy.dpopproofjwt.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Instant

@Entity
class RefreshToken(
  @Column(nullable = false, unique = true)
  val tokenHash: String,
  @Column(nullable = false)
  val username: String,
  @Column(nullable = false)
  val jkt: String,
  @Column(nullable = false)
  val expiresAt: Instant,
  @Id @GeneratedValue
  val id: Long? = null,
) {

  fun isExpired(now: Instant): Boolean = !now.isBefore(expiresAt)

  fun isBoundTo(jkt: String): Boolean = this.jkt == jkt
}
