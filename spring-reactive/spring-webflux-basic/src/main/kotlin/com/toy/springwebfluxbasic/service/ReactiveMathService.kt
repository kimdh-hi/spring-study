package com.toy.springwebfluxbasic.service

import com.toy.springwebfluxbasic.dto.Response
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class ReactiveMathService {

  fun getSquare(input: Int): Mono<Response> {
    return Mono.fromSupplier { input * input }
      .map { Response(output = it) }
  }

  // Thread.sleep 을 사용하는 경우 스트림을 소비하는 클라이언트가 중간에 요청을 취소해도 즉시 producing 이 종료되지 않는다. ( 몇 개 아이템을 더 emit 하고 종료됨)
  // delayElements 와 같은 논블로킹 방식을 사용하는 경우 클라이언트가 중간에 요청을 취소할 때 스트림으로 emit 을 즉시 종료한다.
  fun multiplicationTable(input: Int): Flux<Response> {
    return Flux.range(1, 10)
//      .doOnNext { Thread.sleep(1000L) }
      .delayElements(Duration.ofSeconds(1))
      .doOnNext { println("multiplication table: [$input * $it]") }
      .map { Response(output = input * it) }
  }
}