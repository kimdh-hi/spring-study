package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.domain.User
import com.toy.webfluxr2dbcpostgres.service.UserService
import com.toy.webfluxr2dbcpostgres.vo.UserUpdateRequestVO
import com.toy.webfluxr2dbcpostgres.vo.UserSaveRequestVO
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UserController(
  private val userService: UserService
) {

  @PostMapping
  suspend fun save(@RequestBody requestVO: UserSaveRequestVO) = userService.save(requestVO)

  @GetMapping
  fun list(): Flow<User> = userService.list()

  @GetMapping("/{id}")
  suspend fun get(@PathVariable id: Long) = userService.get(id)

  @PutMapping("/{id}")
  suspend fun update(@PathVariable id: Long, @RequestBody requestVO: UserUpdateRequestVO): ResponseEntity<Unit> {
    userService.update(id, requestVO)

    return ResponseEntity.noContent().build()
  }
}