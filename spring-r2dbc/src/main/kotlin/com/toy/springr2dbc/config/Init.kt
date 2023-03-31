package com.toy.springr2dbc.config

import com.toy.springr2dbc.domain.User
import com.toy.springr2dbc.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

//@Component
class Init(
  private val userRepository: UserRepository
): ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    (1..1_000).forEach {
      userRepository.save(User(name = "user$it"))
    }
  }
}