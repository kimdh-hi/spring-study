package com.toy.springwebfluxgraphql.`lec10-interface`

import com.toy.springwebfluxgraphql.`lec10-interface`.domain.Book
import com.toy.springwebfluxgraphql.`lec10-interface`.domain.Electronics
import com.toy.springwebfluxgraphql.`lec10-interface`.domain.Fruit
import com.toy.springwebfluxgraphql.`lec10-interface`.dto.FruitDto
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Controller
class ProductController {

  /*
  {
    products {
      id
      description
      price
      type: __typename
      ... on Fruit {
        expireDate
      }
      ... on Electronics {
        brand
      }
    }
  }
   */

  @QueryMapping
  fun products(): Flux<Any> = Flux.just(
    FruitDto(description = "banana", price = 100, expireDate = LocalDateTime.now().plusDays(3)),
    FruitDto(description = "apple", price = 200, expireDate = LocalDateTime.now().plusDays(1)),
    Electronics(description = "mac book", price = 200, brand = "APPLE"),
    Electronics(description = "phone", price = 200, brand = "SAMSUNG"),
    Book(description = "java", price = 500, author = "kim"),
    Book(description = "spring", price = 1500, author = "lee"),
  )
}