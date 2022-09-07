package com.toy.userservice.controller

import com.toy.userservice.dto.UserDto
import com.toy.userservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @GetMapping
  fun getAll() = userService.getAll()

  @GetMapping("/{id}")
  fun get(@PathVariable id: Int) = userService.get(id)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @PostMapping
  fun save(@RequestBody requestDtoMono: Mono<UserDto>) = userService.save(requestDtoMono)
    .map { ResponseEntity.ok(it) }

  @PutMapping("/{id}")
  fun update(@PathVariable id: Int, @RequestBody requestDtoMono: Mono<UserDto>) = userService.update(id, requestDtoMono)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: Int) = userService.delete(id)
}