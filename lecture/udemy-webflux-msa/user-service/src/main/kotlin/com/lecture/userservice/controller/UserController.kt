package com.lecture.userservice.controller

import com.lecture.userservice.dto.UserDto
import com.lecture.userservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @GetMapping
  fun getAll(): Flux<UserDto> = userService.getAll()

  @GetMapping("/{id}")
  fun get(@PathVariable id: Int): Mono<ResponseEntity<UserDto>> = userService.get(id)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @PostMapping
  fun save(@RequestBody dto: Mono<UserDto>): Mono<ResponseEntity<UserDto>> = userService.save(dto)
    .map { ResponseEntity.ok(it) }

  @PutMapping("/{id}")
  fun update(@PathVariable id: Int, @RequestBody dto: Mono<UserDto>): Mono<ResponseEntity<UserDto>> = userService.update(id, dto)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: Int): Mono<ResponseEntity<Unit>> = userService.delete(id)
    .map { ResponseEntity.ok().build() }
}