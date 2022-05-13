package com.study.jwt.auth

import com.study.jwt.domain.Account
import com.study.jwt.vo.BaseVO

data class JwtRequestVO(
    val userId: String,
    val username: String,
    val authority: String
): BaseVO() {

    companion object {

        fun fromAccount(account: Account): JwtRequestVO {
            return JwtRequestVO(
                userId = account.id!!,
                username = account.username,
                authority = account.role.getAuthority().toString()
            )
        }

        private const val serialVersionUID: Long = -6094591525487191226L
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