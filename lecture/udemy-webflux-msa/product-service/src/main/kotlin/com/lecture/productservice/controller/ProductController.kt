package com.lecture.productservice.controller

import com.lecture.productservice.dto.ProductDto
import com.lecture.productservice.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/products")
class ProductController(
  private val productService: ProductService
) {

  @GetMapping
  fun getAll(): Flux<ProductDto> = productService.getAll()

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): Mono<ResponseEntity<ProductDto>> = productService.get(id)
    .map { ResponseEntity.ok(it) }

  @PostMapping
  fun save(@RequestBody dto: Mono<ProductDto>): Mono<ResponseEntity<ProductDto>> = productService.save(dto)
    .map { ResponseEntity.ok(it) }

  @PutMapping("/{id}")
  fun update(
    @PathVariable id: String,
    @RequestBody dto: ProductDto
  ) = productService.update(id, dto)
    .map { ResponseEntity.ok(it) }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String): Mono<ResponseEntity<Unit>> = productService.delete(id)
    .map { ResponseEntity.ok().build() }
}