package com.study.gof.`03-adapter`

import com.study.gof.`03-adapter`.adapters.AccountUserDetailsService
import com.study.gof.`03-adapter`.security.LoginHandler

fun main() {

  val accountService = AccountService()
  val userDetailsService = AccountUserDetailsService(accountService)

  val loginHandler = LoginHandler(userDetailsService)

  val login = loginHandler.login("test", "test")
  println(login)
}