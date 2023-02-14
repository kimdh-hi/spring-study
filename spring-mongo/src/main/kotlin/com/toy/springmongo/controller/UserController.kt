package com.toy.springmongo.controller

import com.toy.springmongo.service.UserService
import com.toy.springmongo.vo.UserRequestVO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody vo: UserRequestVO) = userService.save(vo)

  @GetMapping("/{id}")
  fun find(@PathVariable id: String) = userService.find(id)

  @GetMapping
  fun findAll() = userService.findAll()

  @PutMapping("/{id}")
  fun update(@PathVariable id: String, @RequestBody vo: UserRequestVO) = userService.update(id, vo)

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) = userService.delete(id)
}