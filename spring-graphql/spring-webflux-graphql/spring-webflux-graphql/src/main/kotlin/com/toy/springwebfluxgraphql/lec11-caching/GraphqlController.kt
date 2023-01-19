package com.toy.springwebfluxgraphql.`lec11-caching`

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.concurrent.ThreadLocalRandom

@Controller
class GraphqlController {

  @QueryMapping
  fun hello(@Argument("name") value: String): Mono<String> {
    return Mono.just("hello $value")
  }
}
