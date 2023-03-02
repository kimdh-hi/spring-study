package com.toy.springneo4j.test

import com.toy.springneo4j.domain.Account
import com.toy.springneo4j.repository.AccountRepository
import org.neo4j.driver.internal.SessionFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestRunner(
  private val accountRepository: AccountRepository
): ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    val account = Account(username = "user@user")
    accountRepository.save(account)
  }
}