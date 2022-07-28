package com.toy.hibernatelisteners.controller

import com.toy.hibernatelisteners.service.UserService
import com.toy.hibernatelisteners.vo.UserSaveRequestVO
import com.toy.hibernatelisteners.vo.UserUpdateRequestVO
import org.springframework.http.ResponseEntity
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
  fun save(@RequestBody requestVO: UserSaveRequestVO) = ResponseEntity.ok(userService.save(requestVO))

  @PutMapping("/{id}")
  fun update(@PathVariable id: String, @RequestBody requestVO: UserUpdateRequestVO) = ResponseEntity.ok(userService.update(id, requestVO))

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) = ResponseEntity.ok(userService.delete(id))

}