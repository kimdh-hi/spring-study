package com.toy.restdocsdemo

import com.toy.restdocsdemo.domain.User
import com.toy.restdocsdemo.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication
@EnableJpaAuditing
class RestDocsDemoApplication

fun main(args: Array<String>) {
  runApplication<RestDocsDemoApplication>(*args)
}

@Component
class DataInit(
  private val userRepository: UserRepository
): CommandLineRunner {
  override fun run(vararg args: String?) {
    val users = mutableListOf<User>()
    users.add(User(email = "kim@gmail.com", name = "kim"))
    users.add(User(email = "lee@gmail.com", name = "lee"))
    users.add(User(email = "park@gmail.com", name = "park"))

    userRepository.saveAll(users)
  }
}
