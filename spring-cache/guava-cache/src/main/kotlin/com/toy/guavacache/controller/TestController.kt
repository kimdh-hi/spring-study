package com.toy.guavacache.controller

import com.toy.guavacache.cache.CacheStore
import com.toy.guavacache.domain.TestData
import com.toy.guavacache.service.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val testService: TestService,
  private val testDataCache: CacheStore<TestData>
) {

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): TestData {
    testDataCache.get(id)?.let {
      return it
    }

    val testData = testService.get(id)
    testDataCache.put(id, testData)
    return testData
  }
}