package com.toy.webfluxcache.controller

import com.toy.webfluxcache.service.TestService
import com.toy.webfluxcache.vo.TestResponseVO
import org.slf4j.LoggerFactory
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

  @GetMapping("/reactor/default-cacheable")
  fun get(): Mono<ResponseEntity<TestResponseVO>> {
    log.info("get ...")
    return testService.serviceReactorDefault()
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/reactor/reactor-cacheable")
  fun reactor(): ResponseEntity<Mono<TestResponseVO>> {
    log.info("get...")
    val result = testService.serviceReactorCache()
    return ResponseEntity.ok(result)
  }

  @GetMapping("/suspend/default")
  suspend fun suspendDefault(): ResponseEntity<TestResponseVO> {
    log.info("get ...")
    val result = testService.suspendServiceDefault()
    return ResponseEntity.ok(result)
  }

  @GetMapping("/suspend/deferred")
  suspend fun suspendDeferred(): ResponseEntity<TestResponseVO> {
    log.info("get ...")
    val result = testService.suspendServiceAsync().await()
    return ResponseEntity.ok(result)
  }
}