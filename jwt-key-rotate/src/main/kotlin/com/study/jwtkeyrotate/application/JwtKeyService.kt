package com.study.jwtkeyrotate.application

import com.nimbusds.jose.jwk.JWK
import com.study.jwtkeyrotate.common.JwtProperties
import com.study.jwtkeyrotate.domain.model.JwtKey
import com.study.jwtkeyrotate.domain.model.enums.JwtKeyStatus
import com.study.jwtkeyrotate.domain.repository.JwtKeyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class JwtKeyService(
  private val jwtKeyRepository: JwtKeyRepository,
  private val jwtProperties: JwtProperties,
) {
  fun init() {
    findActiveJwtKey() ?: rotate()
  }

  fun rotate(): JwtKey {
    findActiveJwtKey()?.demote()
    return jwtKeyRepository.save(JwtKey())
  }

  fun revoke(kid: String) {
    val key = jwtKeyRepository.findByIdOrNull(kid) ?: return
    if (key.isActive) rotate()
    jwtKeyRepository.delete(key)
  }

  fun cleanup() {
    jwtKeyRepository.deleteByStatusAndDemotedAtBefore(JwtKeyStatus.VERIFY_ONLY, Instant.now() - jwtProperties.ttl)
  }

  @Transactional(readOnly = true)
  fun getSigningKey(): JWK {
    val activeJwtKey = findActiveJwtKey() ?: throw RuntimeException("active jwt key not exists")
    return activeJwtKey.toJwk()
  }

  @Transactional(readOnly = true)
  fun getVerificationKeys(): List<JWK> = jwtKeyRepository.findAll().map { it.toJwk() }

  @Transactional(readOnly = true)
  fun list(): List<JwtKey> = jwtKeyRepository.findAll()

  private fun findActiveJwtKey() = jwtKeyRepository.findByStatus(JwtKeyStatus.ACTIVE)
}
