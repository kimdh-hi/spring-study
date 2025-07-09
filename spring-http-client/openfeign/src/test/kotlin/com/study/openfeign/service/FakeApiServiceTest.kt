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
    fakeApiService.getTodos()
  }

  @Disabled
  @Test
  fun getTodos100() {
    val measureTime = measureTime {
      repeat(100) {
        fakeApiService.getTodos()
      }
    }

    logger.info("measureTime={}", measureTime.inWholeMilliseconds)
  }

  @Test
  fun `async`() = runBlocking {
    val requests = mutableListOf<Deferred<List<TodoResponse>>>()

    repeat(10) {
      val deferred = async(Dispatchers.IO) {
        fakeApiService.getTodos()
      }
      requests.add(deferred)
    }
  }
}
