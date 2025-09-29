package com.study.openfeign.service

import com.study.openfeign.client.DefaultFakeApiClient
import com.study.openfeign.client.FakeApiFeignClient
import com.study.openfeign.client.TodoResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FakeApiService(
  private val fakeApiFeignClient: FakeApiFeignClient,
  private val defaultFakeApiClient: DefaultFakeApiClient,
) {

  private val log = LoggerFactory.getLogger(FakeApiService::class.java)

  fun getTodo(): TodoResponse {
    log.info("isVirtualThread={}", Thread.currentThread().isVirtual)
    return fakeApiFeignClient.getTodo()
  }

  fun getTodoByDefault() = defaultFakeApiClient.getTodo()
}
