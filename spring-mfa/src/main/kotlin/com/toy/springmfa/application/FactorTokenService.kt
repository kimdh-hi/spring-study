package com.toy.springmfa.application

import com.toy.springmfa.infra.config.MfaProperties
import com.toy.springmfa.infra.security.jwt.AccessTokenIssuer
import java.time.Duration
import java.time.Instant
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.FactorGrantedAuthority
import org.springframework.stereotype.Service

@Service
class FactorTokenService(
  private val authenticationManager: AuthenticationManager,
  private val accessTokenIssuer: AccessTokenIssuer,
  private val mfaProperties: MfaProperties,
) {

  fun login(username: String, password: String): String {
    val authentication = authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken.unauthenticated(username, password),
    )
    return issue(authentication, factorsOf(authentication), mfaProperties.jwt.pendingTokenTtl)
  }

  fun stepUp(current: Authentication, request: Authentication): String {
    val verified = authenticationManager.authenticate(request)
    if (verified.name != current.name) throw BadCredentialsException("factor owner mismatch")
    return issue(verified, factorsOf(current) + factorsOf(verified), mfaProperties.jwt.accessTokenTtl)
  }

  private fun issue(authentication: Authentication, factors: Map<String, Instant>, ttl: Duration): String {
    val roles = authentication.authorities
      .filterNot { it is FactorGrantedAuthority }
      .mapNotNull { it.authority }
    return accessTokenIssuer.issue(authentication.name, roles, factors, ttl)
  }

  private fun factorsOf(authentication: Authentication): Map<String, Instant> =
    authentication.authorities
      .filterIsInstance<FactorGrantedAuthority>()
      .filter { it.authority != FactorGrantedAuthority.BEARER_AUTHORITY }
      .associate { it.authority to it.issuedAt }
}
