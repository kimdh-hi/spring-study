package com.study.jwt.service

import com.study.jwt.base.PasswordUtil
import com.study.jwt.domain.Account
import com.study.jwt.repository.AccountRepository
import com.study.jwt.vo.SignupRequestVO
import com.study.jwt.vo.SignupResponseVO
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional
    fun signup(signupRequestVO: SignupRequestVO): SignupResponseVO {
        usernameExistsCheck(signupRequestVO.username)

        val account = signupRequestVO.toAccount()
        val savedAccount = accountRepository.save(account)
        return SignupResponseVO.fromAccount(savedAccount)
    }

    fun authenticate(username: String, password: String): Account {
        accountRepository.findByUsername(username)?.let { account ->
            PasswordUtil.matches(password, account.password)
            return account
        } ?: throw BadCredentialsException("failed to login. user not found ....")
    }

    private fun usernameExistsCheck(username: String) {
        val exists = accountRepository.existsByUsername(username)
        if (exists)
            throw IllegalArgumentException("duplicated username ...")
    }
}