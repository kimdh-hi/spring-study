package com.toy.springcacheex.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CacheableServiceTest @Autowired constructor(
  private val cacheableService: CacheableService
) {

  @Test
  fun test() {
    val result = cacheableService.business()
    println(result)
  }
}