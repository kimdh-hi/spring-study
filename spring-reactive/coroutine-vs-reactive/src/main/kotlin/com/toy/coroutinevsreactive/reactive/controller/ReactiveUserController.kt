package com.toy.coroutinevsreactive.reactive.controller

import com.toy.coroutinevsreactive.reactive.service.ReactiveMemberService
import com.toy.coroutinevsreactive.reactive.vo.UserResponseVO
import com.toy.coroutinevsreactive.reactive.vo.UserSaveRequestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/reactive/users")
class ReactiveUserController(private val reactiveMemberService: ReactiveMemberService) {

  @GetMapping
  fun findByName(@RequestParam username: String) = reactiveMemberService.findByUsername(username)

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = reactiveMemberService.findById(id)

  @PostMapping
  fun save(@RequestBody requestVO: UserSaveRequestVO): Mono<ResponseEntity<UserResponseVO>> =
    reactiveMemberService
      .save(requestVO)
      .flatMap { responseVO -> Mono.just(ResponseEntity.ok(responseVO)) }

  @DeleteMapping
  fun deleteByName(@RequestParam username: String): Mono<Unit> =
    reactiveMemberService.deleteByName(username)

}