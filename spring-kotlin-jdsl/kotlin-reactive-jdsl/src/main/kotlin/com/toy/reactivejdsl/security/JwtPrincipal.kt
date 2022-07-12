package com.toy.webfluxr2dbcpostgres.auth

import com.nimbusds.jwt.JWTClaimsSet
import com.toy.reactivejdsl.domain.User
import java.security.Principal

data class JwtPrincipal(
  val companyId: String? = null,
  val userId: String,
  val username: String,
) : Principal {

  companion object {
    const val CLAIM_USER_ID = "ui"
    const val CLAIM_USERNAME = "un"

    fun of(user: User): JwtPrincipal {

      return JwtPrincipal(
        companyId = user.company.id,
        userId = user.id!!,
        username = user.username,
      )
    }

    fun newPrincipal(jwtClaimsSet: JWTClaimsSet) = JwtPrincipal(
      userId = jwtClaimsSet.getStringClaim(CLAIM_USER_ID),
      username = jwtClaimsSet.getStringClaim(CLAIM_USERNAME),
    )
  }

  override fun getName(): String = username

}