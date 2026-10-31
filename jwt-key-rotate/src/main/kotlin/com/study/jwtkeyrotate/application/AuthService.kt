package com.study.jwtkeyrotate.application

import com.study.jwtkeyrotate.common.JwtProperties
import com.study.jwtkeyrotate.common.JwtProvider
import com.study.jwtkeyrotate.domain.model.RefreshToken
import com.study.jwtkeyrotate.domain.repository.RefreshTokenRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class AuthService(
  private val jwtProvider: JwtProvider,
  private val refreshTokenRepository: RefreshTokenRepository,
  private val properties: JwtProperties,
) {
  fun login(username: String) = issue(username)

  fun refresh(rawToken: String): Tokens? {
    val token = refreshTokenRepository.findByIdOrNull(RefreshToken.hash(rawToken)) ?: return null
    refreshTokenRepository.delete(token)
    if (token.isExpired) return null
    return issue(token.subject)
  }

  private fun issue(subject: String): Tokens {
    val rawRefreshToken = RefreshToken.generate()
    val expiresAt = Instant.now() + properties.refreshTtl
    refreshTokenRepository.save(RefreshToken(RefreshToken.hash(rawRefreshToken), subject, expiresAt))
    return Tokens(jwtProvider.issue(subject), rawRefreshToken)
  }

  data class Tokens(val accessToken: String, val refreshToken: String)
}
