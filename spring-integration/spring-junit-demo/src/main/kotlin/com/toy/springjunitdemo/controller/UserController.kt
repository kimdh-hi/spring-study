package com.toy.springjunitdemo.controller

import com.toy.springjunitdemo.domain.User
import com.toy.springjunitdemo.repository.UserRepository
import com.toy.springjunitdemo.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
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
  fun save(@RequestBody requestVO: UserRequestVO) = userService.save(requestVO)

  @PutMapping("/{id")
  fun update(@PathVariable id: Long, @RequestBody requestVO: UserRequestVO) = userService.update(id, requestVO)

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: Long) = userService.delete(id)
}

data class UserRequestVO(
  val username: String
) {
  fun toEntity() = User(username = username)
}

data class UserResponseVO(
  val id: Long,
  val username: String
) {
  companion object {
    fun of(user: User) = UserResponseVO(
      id = user.id!!,
      username = user.username
    )
  }
}