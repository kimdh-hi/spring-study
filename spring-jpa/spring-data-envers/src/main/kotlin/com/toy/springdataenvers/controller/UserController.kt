package com.toy.springdataenvers.controller

import com.toy.springdataenvers.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping("/{name}")
  fun save(@PathVariable name: String) {
    userService.save(name)
  }

  @PutMapping("/{id}/{name}")
  fun update(@PathVariable id: String, @PathVariable name: String) {
    userService.update(id, name)
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) {
    userService.delete(id)
  }
}