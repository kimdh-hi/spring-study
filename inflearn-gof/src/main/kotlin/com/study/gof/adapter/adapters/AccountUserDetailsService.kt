package com.study.gof.adapter.adapters

import com.study.gof.adapter.AccountService
import com.study.gof.adapter.security.UserDetails
import com.study.gof.adapter.security.UserDetailsService

class AccountUserDetailsService(
  private val accountService: AccountService
): UserDetailsService {

  override fun loadUser(username: String): UserDetails {
    val account = accountService.findByUsername(username)
    return AccountUserDetails(account)
  }
}