package com.study.jwt.auth

import com.nimbusds.jwt.JWTClaimsSet
import com.study.jwt.domain.Account
import com.study.jwt.vo.BaseVO
import java.security.Principal

data class JwtPrincipal(
    val userId: String,
    val username: String,
    val authority: String
): Principal {

    companion object {

        fun fromAccount(account: Account): JwtPrincipal {
            return JwtPrincipal(
                userId = account.id!!,
                username = account.username,
                authority = account.role.getAuthority().toString()
            )
        }

        fun fromJwtClaims(jwtClaimsSet: JWTClaimsSet): JwtPrincipal {
            return JwtPrincipal(
                userId = jwtClaimsSet.getStringClaim("userId"),
                username = jwtClaimsSet.getStringClaim("username"),
                authority = jwtClaimsSet.getStringClaim("authority")
            )

        }

        private const val serialVersionUID: Long = -6094591525487191226L
    }

    override fun getName(): String {
        return username
    }
}

data class JwtResponseVO (
    val token: String
): BaseVO() {

    companion object {

        fun newInstance(token: String): JwtResponseVO {
            return JwtResponseVO(
                token = token
            )
        }

        private const val serialVersionUID: Long = 3221200770322550474L
    }
}