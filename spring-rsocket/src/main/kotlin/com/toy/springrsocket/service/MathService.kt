package com.toy.springrsocket.service

import com.toy.springrsocket.dto.ChartResponseDto
import com.toy.springrsocket.dto.ComputationRequestDto
import com.toy.springrsocket.dto.ComputationResponseDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MathService {

  fun print(dto: Mono<ComputationRequestDto>): Mono<Void> {
    return dto.doOnNext { println(it) }
      .then()
  }

  fun square(dto: Mono<ComputationRequestDto>): Mono<ComputationResponseDto> {
    return dto
      .map { it.input }
      .map { ComputationResponseDto(it, it * it) }
  }

  fun tableStream(dto: ComputationRequestDto): Flux<ComputationResponseDto> {
    return Flux.range(1, 10)
      .map { ComputationResponseDto(dto.input, dto.input * it) }
  }

  fun chartStream(dto: Flux<ComputationRequestDto>): Flux<ChartResponseDto> {
    return dto
      .map { it.input }
      .map { ChartResponseDto(it, (it * it) + 1) }
  }
}