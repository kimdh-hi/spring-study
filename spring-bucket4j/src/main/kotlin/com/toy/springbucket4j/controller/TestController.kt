package com.toy.springbucket4j.controller

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration


@RestController
@RequestMapping("/api/test")
class TestController {

  private val log = LoggerFactory.getLogger(javaClass)

  val bucket: Bucket by lazy {
    // bucket 에 token 은 3개
    // 1분마다 bucket 에 3개 token 충전
    val limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1)))
    Bucket.builder()
      .addLimit(limit)
      .build()
  }

  @GetMapping
  fun test(): ResponseEntity<String> {
    if (bucket.tryConsume(1)) {
      log.info("api call success")
      return ResponseEntity.ok("ok")
    }

    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build()
  }
}