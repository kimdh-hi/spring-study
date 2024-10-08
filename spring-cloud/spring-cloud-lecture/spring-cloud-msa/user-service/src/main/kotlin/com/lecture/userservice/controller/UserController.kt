package com.lecture.userservice.controller

import com.lecture.userservice.common.GreetingProperties
import com.lecture.userservice.service.UserService
import com.lecture.userservice.vo.UserResponseVO
import com.lecture.userservice.vo.UserSaveRequestVO
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService,
  private val greetingProperties: GreetingProperties,
) {

  @PostMapping
  fun save(@RequestBody vo: UserSaveRequestVO): ResponseEntity<UserResponseVO> {
    val responseVO = userService.save(vo)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping
  fun findAll(): ResponseEntity<List<UserResponseVO>> {
    val responseVO = userService.findAll()
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: String): ResponseEntity<UserResponseVO> {
    val responseVO = userService.findById(id)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/welcome")
  fun welcome() = greetingProperties.message
}