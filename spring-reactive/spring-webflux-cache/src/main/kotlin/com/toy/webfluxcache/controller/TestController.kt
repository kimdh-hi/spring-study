package com.toy.webfluxcache.controller

import com.toy.webfluxcache.service.TestService
import com.toy.webfluxcache.vo.TestResponseVO
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/test")
class TestController(
  private val testService: TestService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  @Cacheable("test")
  fun get(): ResponseEntity<Mono<TestResponseVO>> {
    log.info("get ...")
    val result = testService.service()
    return ResponseEntity.ok(result)
  }

  // caching with suspend ...
  @GetMapping("/suspend")
  suspend fun suspend(): ResponseEntity<TestResponseVO> {
    log.info("get ...")
    val result = testService.suspendServiceAsync().await()
    return ResponseEntity.ok(result)
  }

  @GetMapping("/reactor")
  fun reactor(): ResponseEntity<Mono<TestResponseVO>> {
    log.info("get...")
    val result = testService.serviceReactive()
    return ResponseEntity.ok(result)
  }
}