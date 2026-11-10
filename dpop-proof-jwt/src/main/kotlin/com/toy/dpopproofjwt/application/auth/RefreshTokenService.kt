package com.toy.dpopproofjwt.application.auth

import com.toy.dpopproofjwt.common.exception.InvalidRefreshTokenException
import com.toy.dpopproofjwt.domain.model.RefreshToken
import com.toy.dpopproofjwt.domain.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.Base64

@Service
class RefreshTokenService(
  private val refreshTokenRepository: RefreshTokenRepository,
  private val proofVerifier: DPoPProofVerifier,
  private val tokenIssuer: TokenIssuer,
) {

  private val ttl: Duration = Duration.ofDays(1)
  private val random = SecureRandom()

  @Transactional
  fun issue(username: String, jkt: String): IssuedToken {
    val value = ByteArray(32).also(random::nextBytes).let(ENCODER::encodeToString)
    refreshTokenRepository.save(RefreshToken(hash(value), username, jkt, Instant.now().plus(ttl)))
    return IssuedToken(tokenIssuer.issue(username, jkt), value)
  }

  @Transactional(noRollbackFor = [InvalidRefreshTokenException::class])
  fun refresh(command: RefreshCommand): IssuedToken {
    val jkt = proofVerifier.verify(command.proof)
    val tokenHash = hash(command.refreshToken)
    val stored = refreshTokenRepository.findByTokenHash(tokenHash) ?: throw InvalidRefreshTokenException()

    if (refreshTokenRepository.deleteByTokenHash(tokenHash) == 0) throw InvalidRefreshTokenException()
    if (stored.isExpired(Instant.now())) throw InvalidRefreshTokenException()
    if (!stored.isBoundTo(jkt)) throw InvalidRefreshTokenException("Refresh token is not bound to the DPoP proof key.")

    return issue(stored.username, jkt)
  }

  private fun hash(value: String): String =
    ENCODER.encodeToString(MessageDigest.getInstance("SHA-256").digest(value.toByteArray()))

  companion object {
    private val ENCODER = Base64.getUrlEncoder().withoutPadding()
  }
}

data class RefreshCommand(
  val refreshToken: String,
  val proof: DPoPProof,
)
