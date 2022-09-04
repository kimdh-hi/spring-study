package com.toy.springwebfluxbasic.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/params")
class QueryParamController {

  @GetMapping
  fun params(@RequestParam("size") size: Int, @RequestParam("page") page: Int): Flux<Int> {
    return Flux.just(size, page)
  }
}