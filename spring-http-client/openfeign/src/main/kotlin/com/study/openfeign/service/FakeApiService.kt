package com.study.openfeign.service

import com.study.openfeign.client.DefaultFakeApiClient
import com.study.openfeign.client.FakeApiFeignClient
import org.springframework.stereotype.Service

@Service
class FakeApiService(
  private val fakeApiFeignClient: FakeApiFeignClient,
  private val defaultFakeApiClient: DefaultFakeApiClient,
) {

  fun getTodo() = fakeApiFeignClient.getTodo()

  fun getTodoByDefault() = defaultFakeApiClient.getTodo()
}
