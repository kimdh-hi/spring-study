package com.toy.springcacheex.controller

import com.toy.springcacheex.domain.User
import com.toy.springcacheex.repository.UserRepository
import com.toy.springcacheex.service.UserService
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): ResponseEntity<User> {
    val user = userService.get(id)

    return ResponseEntity.ok(user)
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String): ResponseEntity<Unit> {
    userService.delete(id)

    return ResponseEntity.ok().build()
  }
}
