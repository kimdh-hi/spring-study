package com.toy.springcacheex.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CacheTestServiceTest(
  private val cacheTestService: CacheTestService
) {

  @Test
  fun test() {
    val result = cacheTestService.getSpaceParticipantCount("space1", "ch1")

    println(result)
  }
}