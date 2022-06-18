package com.toy.springcacheex.controller

import com.toy.springcacheex.domain.User
import com.toy.springcacheex.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

  @GetMapping("/{id}")
  @Cacheable(value = ["user-get"], key = "#id")
  fun get(@PathVariable id: String): ResponseEntity<User> {
    val user = userRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("not found ...")
    return ResponseEntity.ok(user)
  }
}
