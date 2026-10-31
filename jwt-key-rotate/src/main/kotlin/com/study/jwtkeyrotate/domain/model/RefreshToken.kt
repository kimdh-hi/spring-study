package com.study.jwtkeyrotate.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64

@Entity
class RefreshToken(
  @Id val tokenHash: String,
  val subject: String,
  val expiresAt: Instant,
) {
  val isExpired get() = expiresAt < Instant.now()

  companion object {
    fun generate(): String =
      Base64.getUrlEncoder().withoutPadding().encodeToString(ByteArray(32).also { SecureRandom().nextBytes(it) })

    fun hash(raw: String): String =
      Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(raw.toByteArray()))
  }
}
