package com.toy.springmfa.auth

import com.toy.springmfa.token.AccessTokenIssuer
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.FactorGrantedAuthority
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FactorTokenService(
  private val authenticationManager: AuthenticationManager,
  private val accessTokenIssuer: AccessTokenIssuer,
) {

  fun login(username: String, password: String): String {
    val authentication = authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken.unauthenticated(username, password),
    )
    return issue(authentication, factorsOf(authentication))
  }

  fun stepUp(current: Authentication, request: Authentication): String {
    val verified = authenticationManager.authenticate(request)
    if (verified.name != current.name) throw BadCredentialsException("factor owner mismatch")
    return issue(verified, factorsOf(current) + factorsOf(verified))
  }

  private fun issue(authentication: Authentication, factors: Map<String, Instant>): String {
    val roles = authentication.authorities
      .mapNotNull { it.authority }
      .filter { it.startsWith("ROLE_") }
      .map { it.removePrefix("ROLE_") }
    return accessTokenIssuer.issue(authentication.name, roles, factors)
  }

  private fun factorsOf(authentication: Authentication): Map<String, Instant> =
    authentication.authorities
      .filterIsInstance<FactorGrantedAuthority>()
      .filter { it.authority != FactorGrantedAuthority.BEARER_AUTHORITY }
      .associate { it.authority to it.issuedAt }
}
