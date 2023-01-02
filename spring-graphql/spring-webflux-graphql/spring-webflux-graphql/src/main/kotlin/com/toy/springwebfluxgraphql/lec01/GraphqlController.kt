package com.toy.springwebfluxgraphql.lec01

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.concurrent.ThreadLocalRandom

@RestController
class GraphqlController {

  /**
   * 기본적으로 schema.graphqls 에 정의한 이름과 메서드 명이 동일해야 함
   * 굳이 다르게하고 싶다면 name 으로 매핑하여 사용
   */
  @QueryMapping("sayHello")
  fun sayHello222(): Mono<String> {
    return Mono.just("hello")
  }

  /*
  {
    sayHelloTo(name: "kim")
  }
   */
  @QueryMapping
  fun sayHelloTo(@Argument name: String): Mono<String> {
    return Mono.fromSupplier { "hello $name" }
  }

  @QueryMapping
  fun random() = Mono.just(ThreadLocalRandom.current().nextInt(1, 100))
}

/**
 * 위 3개 메서드가 api 의 엔드포인트라고 가정해보자.
 * 그리고 클라이언트는 3개 api 의 응답을 모두 필요로 한다.
 * 전통적인 api 방식의 경우 3번의 호출 또는 해당 요청을 위한 새로운 api 를 추가해야 한다.
 * msa 와 같이 각 api 가 완전히 분리되었다면 통합된 응답을 위한 api 를 추가하는 것도 불가능하다.
 *
 * graphql 의 경우 위 3개 요청을 한 번에 질의하고 응답받을 수 있다.
 */

/*
query
{
  sayHello,
  sayHelloTo(name: "kim"),
  random
}

response
{
  "data": {
    "sayHello": "hello",
    "sayHelloTo": "hello kim",
    "random": 82
  }
}
 */