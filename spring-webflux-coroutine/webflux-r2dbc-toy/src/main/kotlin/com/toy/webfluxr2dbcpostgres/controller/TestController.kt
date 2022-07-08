package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.service.TestService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val testService: TestService
) {

  @GetMapping("/sync")
  fun getSync() = testService.callSyncJob()

  @GetMapping("/numbers", produces = [MediaType.APPLICATION_NDJSON_VALUE])
  fun getNumbers(): Flow<Int> = flow {
    1
  }
}