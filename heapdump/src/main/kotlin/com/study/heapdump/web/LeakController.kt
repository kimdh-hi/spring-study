package com.study.heapdump.web

import com.study.heapdump.domain.Product
import com.study.heapdump.domain.ProductRepository
import com.study.heapdump.service.SeedService
import java.util.concurrent.ConcurrentHashMap
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LeakController(
  private val seedService: SeedService,
  private val productRepository: ProductRepository,
) {

  @GetMapping("/seed")
  fun seed(@RequestParam(defaultValue = "500000") count: Int): String {
    seedService.seed(count)
    return "seeded $count products (chunked flush/clear)"
  }

  @GetMapping("/leak/find-all")
  fun findAll(): String {
    val all = productRepository.findAll()
    return "loaded ${all.size} products into memory"
  }

  @GetMapping("/leak/persistence-context")
  fun persistenceContext(@RequestParam(defaultValue = "500000") count: Int): String {
    seedService.seedWithoutClear(count)
    return "inserted $count products in one persistence context (no clear)"
  }

  @GetMapping("/leak/static-cache")
  fun staticCache(@RequestParam(defaultValue = "10000") count: Int): String {
    val page = productRepository.findAll(Pageable.ofSize(count)).content
    page.forEach { cache["${it.id}-${System.nanoTime()}"] = it }
    return "cache size = ${cache.size}"
  }

  companion object {
    private val cache = ConcurrentHashMap<String, Product>()
  }
}
