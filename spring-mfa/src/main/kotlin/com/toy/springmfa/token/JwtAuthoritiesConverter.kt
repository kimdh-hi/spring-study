package com.toy.springmfa.token

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.FactorGrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class JwtAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {

  override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
    val roles = jwt.getClaimAsStringList(AccessTokenIssuer.ROLES_CLAIM).orEmpty()
      .map { SimpleGrantedAuthority("ROLE_$it") }
    val factors = jwt.getClaimAsMap(AccessTokenIssuer.FACTORS_CLAIM).orEmpty()
      .map { (authority, issuedAt) ->
        FactorGrantedAuthority.withAuthority(authority)
          .issuedAt(Instant.ofEpochSecond((issuedAt as Number).toLong()))
          .build()
      }
    return roles + factors
  }
}
