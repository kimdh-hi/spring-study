package com.toy.springrsocket.controller

import com.toy.springrsocket.dto.ChartResponseDto
import com.toy.springrsocket.dto.ComputationRequestDto
import com.toy.springrsocket.dto.ComputationResponseDto
import com.toy.springrsocket.service.MathService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class MathController(
  private val mathService: MathService
) {

  @MessageMapping("math.service.print")
  fun print(dto: Mono<ComputationRequestDto>): Mono<Void> {
    return mathService.print(dto)
  }

  @MessageMapping("math.service.square")
  fun square(dto: Mono<ComputationRequestDto>): Mono<ComputationResponseDto> {
    return mathService.square(dto)
  }

  @MessageMapping("math.service.table")
  fun tableStream(dto: Mono<ComputationRequestDto>): Flux<ComputationResponseDto> {
    return dto.flatMapMany { mathService.tableStream(it) }
  }

  @MessageMapping("math.service.chart")
  fun chartStream(dto: Flux<ComputationRequestDto>): Flux<ChartResponseDto> {
    return mathService.chartStream(dto)
  }
}