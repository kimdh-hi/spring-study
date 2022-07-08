package com.toy.webfluxr2dbcpostgres.auth

import com.nimbusds.jwt.JWTClaimsSet
import com.toy.webfluxr2dbcpostgres.domain.User
import com.toy.webfluxr2dbcpostgres.vo.LoginRequestVO
import java.security.Principal

data class JwtPrincipal(
  val companyId: Long? = null,
  val userId: Long,
  val username: String,
) : Principal {

  companion object {
    const val CLAIM_USER_ID = "ui"
    const val CLAIM_USERNAME = "un"

    fun of(user: User): JwtPrincipal {

      return JwtPrincipal(
//        companyId = user.companyId,
        userId = user.id!!,
        username = user.username,
      )
    }

    fun newPrincipal(jwtClaimsSet: JWTClaimsSet) = JwtPrincipal(
      userId = jwtClaimsSet.getStringClaim(CLAIM_USER_ID).toLong(),
      username = jwtClaimsSet.getStringClaim(CLAIM_USERNAME),
    )
  }

  override fun getName(): String = username

}