package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.service.UserService
import com.toy.reactivejdsl.vo.UserResponseVO
import com.toy.reactivejdsl.vo.UserSaveRequestVO
import com.toy.reactivejdsl.vo.UserSaveResponseVO
import com.toy.reactivejdsl.vo.UserSearchVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  suspend fun save(@RequestBody requestVO: UserSaveRequestVO): ResponseEntity<UserSaveResponseVO> {
    val responseVO = userService.save(requestVO)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping
  suspend fun search(requestVO: UserSearchVO, pageable: Pageable): ResponseEntity<Page<UserResponseVO>> {
    val page = userService.search(pageable, requestVO)
    return ResponseEntity.ok(page)
  }

  @GetMapping("/{id}")
  suspend fun getByName(@PathVariable id: String): ResponseEntity<UserResponseVO> {
    val user = userService.get(id)

    return ResponseEntity.ok(user)
  }
}