package com.study.gof.`03-adapter`.adapters

import com.study.gof.`03-adapter`.AccountService
import com.study.gof.`03-adapter`.security.UserDetails
import com.study.gof.`03-adapter`.security.UserDetailsService

class AccountUserDetailsService(
  private val accountService: AccountService
): UserDetailsService {

  override fun loadUser(username: String): UserDetails {
    val account = accountService.findByUsername(username)
    return AccountUserDetails(account)
  }
}