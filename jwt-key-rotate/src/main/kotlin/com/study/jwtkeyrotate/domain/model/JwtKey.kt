package com.study.jwtkeyrotate.domain.model

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.study.jwtkeyrotate.common.SecretConverter
import com.study.jwtkeyrotate.domain.model.enums.JwtKeyStatus
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.security.SecureRandom
import java.time.Instant

@Entity
class JwtKey(
  @Id
  @UuidGenerator
  var kid: String? = null,

  @Convert(converter = SecretConverter::class)
  val secret: ByteArray = randomSecret(),

  @Enumerated(EnumType.STRING)
  var status: JwtKeyStatus = JwtKeyStatus.ACTIVE,

  val createdAt: Instant = Instant.now(),

  var demotedAt: Instant? = null,
) {

  val isActive get() = status == JwtKeyStatus.ACTIVE

  fun demote() {
    status = JwtKeyStatus.VERIFY_ONLY
    demotedAt = Instant.now()
  }

  fun toJwk(): OctetSequenceKey = OctetSequenceKey.Builder(secret).keyID(kid).algorithm(JWSAlgorithm.HS256).build()

  companion object {
    private fun randomSecret() = ByteArray(32).also { SecureRandom().nextBytes(it) }
  }
}
