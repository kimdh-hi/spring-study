package com.study.jwt.vo

import com.study.jwt.base.PasswordUtil
import com.study.jwt.base.TestData
import com.study.jwt.domain.Account
import com.study.jwt.domain.Role

data class SignupRequestVO(
    val username: String,
    val password: String
): BaseVO() {

    fun toAccount(): Account {
        return Account.newInstance(
            username = username,
            password = PasswordUtil.encode(password), role = Role(id = TestData.ROLE_USER_ID))
    }

    companion object {
        private const val serialVersionUID: Long = 992232603677769187L
    }
}

data class SignupResponseVO(
    val id: String,
    val username: String,
): BaseVO() {

    companion object {
        fun fromAccount(account: Account): SignupResponseVO {
            return SignupResponseVO (
                id = account.id!!, username = account.username)
        }

        private const val serialVersionUID: Long = -2383281003404884834L
    }
}


data class LoginRequestVO (
    val username: String,
    val password: String,
): BaseVO() {

    companion object {
        private const val serialVersionUID: Long = -8729089149853359450L
    }

}
