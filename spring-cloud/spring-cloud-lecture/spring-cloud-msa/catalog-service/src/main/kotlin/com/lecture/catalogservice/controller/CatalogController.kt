package com.lecture.catalogservice.controller

import com.lecture.catalogservice.service.CatalogService
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/catalog-service/catalogs")
class CatalogController(
  private val catalogService: CatalogService,
  private val env: Environment
) {

  @GetMapping
  fun findAll() = ResponseEntity.ok(catalogService.findAll())

  @GetMapping("/health-check")
  fun healthCheck() = "catalog service ok port: ${env.getProperty("local.server.port")}"
}