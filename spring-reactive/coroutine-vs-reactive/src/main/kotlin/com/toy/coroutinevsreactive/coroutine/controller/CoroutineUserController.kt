package com.toy.coroutinevsreactive.coroutine.controller

import com.toy.coroutinevsreactive.coroutine.service.CoroutineUserService
import com.toy.coroutinevsreactive.coroutine.vo.UserResponseVO
import com.toy.coroutinevsreactive.coroutine.vo.UserSaveRequestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/coroutine/users")
class CoroutineUserController(private val coroutineUserService: CoroutineUserService) {

  @GetMapping
  suspend fun findByName(@RequestParam username: String) =
    coroutineUserService.findByUsername(username)

  @GetMapping("/{id}")
  suspend fun findById(@PathVariable id: Long) = coroutineUserService.findById(id)

  @PostMapping
  suspend fun save(@RequestBody requestVO: UserSaveRequestVO): ResponseEntity<UserResponseVO> {
    val responseVO = coroutineUserService.save(requestVO)
    return ResponseEntity.ok(responseVO)
  }

  @DeleteMapping
  suspend fun deleteByName(@RequestParam username: String): ResponseEntity<Unit> {
    return ResponseEntity.ok(coroutineUserService.deleteByName(username))
  }

}