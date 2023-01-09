package com.lecture.productservice.controller

import com.lecture.productservice.service.ProductService
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product-service/products")
class ProductController(
  private val productService: ProductService,
  private val env: Environment
) {

  @GetMapping
  fun findAll() = ResponseEntity.ok(productService.findAll())

  @GetMapping("/health-check")
  fun healthCheck() = "product service ok port: ${env.getProperty("local.server.port")}"
}