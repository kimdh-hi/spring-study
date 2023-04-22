package com.lecture.productservice.common

import com.lecture.productservice.dto.ProductDto
import com.lecture.productservice.service.ProductService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class DataInit(
  private val productService: ProductService
): ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    val item1 = ProductDto(description = "item1", price = 1000)
    val item2 = ProductDto(description = "item2", price = 2000)
    val item3 = ProductDto(description = "item3", price = 3000)
    val item4 = ProductDto(description = "item4", price = 4000)

    Flux.just(item1, item2, item3, item4)
      .flatMap { productService.save(Mono.just(it)) }
      .subscribe { println("data init-product save $it") }
  }
}