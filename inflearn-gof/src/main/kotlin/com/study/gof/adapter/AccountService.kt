package com.study.gof.adapter

class AccountService {

  fun findByUsername(username: String) = Account(name = "test", username = username, password = "test")
}