package com.lecture.userservice.controller

import com.lecture.userservice.common.GreetingProperties
import com.lecture.userservice.service.UserService
import com.lecture.userservice.vo.UserResponseVO
import com.lecture.userservice.vo.UserSaveRequestVO
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user-service/users")
class UserController(
  private val userService: UserService,
  private val greetingProperties: GreetingProperties,
  private val env: Environment
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
  fun findById(@PathVariable id: Long): ResponseEntity<UserResponseVO> {
    val responseVO = userService.findById(id)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/health-check")
  fun healthCheck() = "user service ok port: ${env.getProperty("local.server.port")}"

  @GetMapping("/welcome")
  fun welcome() = greetingProperties.message
}