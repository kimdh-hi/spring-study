package com.toy.springcacheex.controller

import com.toy.springcacheex.domain.User
import com.toy.springcacheex.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

  // user::id-01
  @GetMapping("/{id}")
  @Cacheable(value = ["user"], key = "#id")
  fun get(@PathVariable id: String): User {
    val user = userRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("not found ...")
    return user
  }
}
