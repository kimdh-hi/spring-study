package com.toy.restdocsdemo.controller

import com.toy.restdocsdemo.repository.UserRepository
import com.toy.restdocsdemo.service.UserService
import com.toy.restdocsdemo.vo.UserCreateRequestVO
import com.toy.restdocsdemo.vo.UserResponseVO
import com.toy.restdocsdemo.vo.UserUpdateRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun create(@RequestBody requestVO: UserCreateRequestVO) = ResponseEntity.ok(userService.save(requestVO))

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long) = ResponseEntity.ok(userService.get(id))

  @GetMapping
  fun list() = ResponseEntity.ok(userService.list())

  @PutMapping
  fun update(@RequestBody requestVO: UserUpdateRequestVO) = ResponseEntity.noContent()
}