package com.toy.jparoutingdatasource.controller

import com.toy.jparoutingdatasource.domain.User
import com.toy.jparoutingdatasource.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody user: User) = userService.save(user)

  @GetMapping("/{id}")
  fun get(@PathVariable id: String) = userService.get(id)
}