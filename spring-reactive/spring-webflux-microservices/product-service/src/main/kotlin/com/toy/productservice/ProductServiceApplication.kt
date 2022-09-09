package com.toy.productservice

import com.toy.productservice.dto.ProductDto
import com.toy.productservice.service.ProductService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootApplication
class ProductServiceApplication

fun main(args: Array<String>) {
	runApplication<ProductServiceApplication>(*args)
}

@Component
class SampleDataSetup(
	private val productService: ProductService
): CommandLineRunner {
	override fun run(vararg args: String?) {
		val p1 = ProductDto(description = "p1", price = 1000)
		val p2 = ProductDto(description = "p2", price = 500)
		val p3 = ProductDto(description = "p3", price = 100)
		val p4 = ProductDto(description = "p4", price = 3000)
		val p5 = ProductDto(description = "p5", price = 1500)

		Flux.just(p1, p2, p3 ,p4)
			.flatMap { productService.save(Mono.just(it)) }
			.subscribe { println(it) }
	}
}
