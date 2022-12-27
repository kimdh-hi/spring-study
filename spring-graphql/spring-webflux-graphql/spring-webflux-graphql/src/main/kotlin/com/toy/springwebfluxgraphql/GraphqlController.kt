package com.toy.springwebfluxgraphql

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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
}