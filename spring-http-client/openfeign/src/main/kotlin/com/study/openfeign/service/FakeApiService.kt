package com.study.openfeign.service

import com.study.openfeign.client.FakeApiFeignClient
import org.springframework.stereotype.Service

@Service
class FakeApiService(
  private val fakeApiFeignClient: FakeApiFeignClient,
) {

  fun getTodos() = fakeApiFeignClient.getTodos()
}
