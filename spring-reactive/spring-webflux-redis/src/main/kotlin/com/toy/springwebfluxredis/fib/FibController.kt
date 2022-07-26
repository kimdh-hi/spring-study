package com.toy.springwebfluxredis.fib

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/api/fib")
@RestController
class FibController(
  private val fibService: FibService
) {

  @GetMapping("/{n}/{name}")
  fun fib(@PathVariable n: Int, @PathVariable name: String): Mono<Int> {
    return Mono.fromSupplier { fibService.getFib(n, name) }
  }

  @GetMapping("/{n}/clear")
  fun fibClear(@PathVariable n: Int): Mono<Unit> {
    return Mono.fromSupplier { fibService.clearCache(n) }
  }
}