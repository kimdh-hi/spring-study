package com.study.openfeign.service

import com.study.openfeign.client.TodoResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.measureTime

@SpringBootTest
class FakeApiServiceTest @Autowired constructor(
  private val fakeApiService: FakeApiService,
) {

  private val logger = LoggerFactory.getLogger(FakeApiService::class.java)

  @Test
  fun getTodos() {
    repeat(10) {
      fakeApiService.getTodo()
    }
  }

  @Disabled
  @Test
  fun getTodos100() {
    val measureTime = measureTime {
      repeat(100) {
        fakeApiService.getTodo()
      }
    }

    logger.info("measureTime={}", measureTime.inWholeMilliseconds)
  }

  @Test
  fun async() = runBlocking {
    repeat(10) {
      async(Dispatchers.IO) {
        fakeApiService.getTodo()
      }
    }
  }
}
