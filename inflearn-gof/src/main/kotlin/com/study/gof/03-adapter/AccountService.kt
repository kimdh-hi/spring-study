package com.study.gof.`03-adapter`

class AccountService {

  fun findByUsername(username: String) = Account(name = "test", username = username, password = "test")
}