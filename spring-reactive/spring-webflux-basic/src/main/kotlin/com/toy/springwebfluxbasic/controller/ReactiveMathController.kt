package com.toy.springwebfluxbasic.controller

import com.toy.springwebfluxbasic.dto.MultiplyRequestDto
import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.service.ReactiveMathService
import org.springframework.http.MediaType
import org.springframework.http.codec.json.AbstractJackson2Encoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive-math")
class ReactiveMathController(
  private val mathService: ReactiveMathService
) {

  @GetMapping("/square/{input}")
  fun getSquare(@PathVariable input: Int): Mono<Response> {
    return mathService.getSquare(input)
  }

  @GetMapping("/table/{input}")
  fun multiplicationTable(@PathVariable input: Int): Flux<Response> {
    // 반환시 AbstractJackson2Encoder.encode() 호출
    return mathService.multiplicationTable(input)
  }

  @GetMapping("/table/{input}/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun multiplicationTableStream(@PathVariable input: Int): Flux<Response> {
    return mathService.multiplicationTable(input)
  }

  @PostMapping("/multiply")
  fun multiply(
    @RequestBody requestDto: Mono<MultiplyRequestDto>,
    @RequestHeader requestHeaders: Map<String, String>
  ): Mono<Response> {
    println(requestHeaders)
    return mathService.multiply(requestDto)
  }

}