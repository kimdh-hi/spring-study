package com.toy.productservice.controller

import com.toy.productservice.dto.ProductDto
import com.toy.productservice.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/products")
class ProductController(
  private val productService: ProductService
) {

  @GetMapping
  fun getAll() = productService.getAll()

  @GetMapping("/{id}")
  fun get(@PathVariable id: String) = productService.getById(id)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @PostMapping
  fun save(@RequestBody requestVO: Mono<ProductDto>) = productService.save(requestVO)
    .map { ResponseEntity.ok(it) }

  @PutMapping("/{id}")
  fun update(
    @PathVariable id: String,
    @RequestBody requestVO: Mono<ProductDto>) = productService.update(id, requestVO)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) = productService.delete(id)
    .map { ResponseEntity.noContent() }
}