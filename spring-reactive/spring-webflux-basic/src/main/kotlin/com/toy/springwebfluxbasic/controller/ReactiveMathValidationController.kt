package com.toy.springwebfluxbasic.controller

import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.exception.InputValidationException
import com.toy.springwebfluxbasic.service.ReactiveMathService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive-math")
class ReactiveMathValidationController(
  private val mathService: ReactiveMathService
) {

  @GetMapping("/square/{input}/error/throw")
  fun throwError(@PathVariable input: Int): Mono<Response> {
    if(input < 10 || input > 20)
      throw InputValidationException()
    return mathService.getSquare(input)
  }

  @GetMapping("/square/{input}/error/throw/mono")
  fun monoError(@PathVariable input: Int): Mono<Response> {
    return Mono.just(input)
      .handle<Int> { param, sink ->
        if (param in 10..20)
          sink.next(param)
        else
          sink.error(InputValidationException())
      }
      .flatMap { mathService.getSquare(it) }
  }

  @GetMapping("/square/{input}/error/filter")
  fun monoFilter(@PathVariable input: Int): Mono<ResponseEntity<Response>> {
    return Mono.just(input)
      .filter { it in 10..20 }
      .flatMap { mathService.getSquare(it) }
      .map { ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.badRequest().build())
  }
}