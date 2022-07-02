package com.study.gof.adapter

import com.study.gof.adapter.adapters.AccountUserDetailsService
import com.study.gof.adapter.security.LoginHandler

fun main() {

  val accountService = AccountService()
  val userDetailsService = AccountUserDetailsService(accountService)

  val loginHandler = LoginHandler(userDetailsService)

  val login = loginHandler.login("test", "test")
  println(login)
}