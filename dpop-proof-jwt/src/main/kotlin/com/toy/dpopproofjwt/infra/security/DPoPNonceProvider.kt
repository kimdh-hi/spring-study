package com.toy.dpopproofjwt.infra.security

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class DPoPNonceProvider(
  private val secret: ByteArray = randomSecret(),
  private val step: Duration = Duration.ofMinutes(1),
  private val clock: Clock = Clock.systemUTC(),
) {

  fun issue(at: Instant = clock.instant()): String {
    val counter = counter(at)
    val bytes = ByteBuffer.allocate(Long.SIZE_BYTES + MAC_LENGTH)
      .putLong(counter)
      .put(mac(counter))
      .array()
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }

  fun isValid(nonce: String): Boolean {
    val bytes = runCatching { Base64.getUrlDecoder().decode(nonce) }.getOrNull() ?: return false
    if (bytes.size != Long.SIZE_BYTES + MAC_LENGTH) return false

    val counter = ByteBuffer.wrap(bytes, 0, Long.SIZE_BYTES).long
    val current = counter(clock.instant())
    return !(counter != current && counter != current - 1) && MessageDigest.isEqual(bytes.copyOfRange(Long.SIZE_BYTES, bytes.size), mac(counter))
  }

  private fun counter(at: Instant): Long = at.toEpochMilli() / step.toMillis()

  private fun mac(counter: Long): ByteArray =
    Mac.getInstance(ALGORITHM)
      .apply { init(SecretKeySpec(secret, ALGORITHM)) }
      .doFinal(ByteBuffer.allocate(Long.SIZE_BYTES).putLong(counter).array())

  companion object {
    private const val ALGORITHM = "HmacSHA256"
    private const val MAC_LENGTH = 32

    private fun randomSecret(): ByteArray = ByteArray(32).also { SecureRandom().nextBytes(it) }
  }
}
