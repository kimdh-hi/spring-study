package com.toy.springwebclient.service

import com.toy.springwebclient.utils.WebClientUtils
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TestService(
  private val webClientUtils: WebClientUtils
) {

  fun test(): Mono<FakeUser> {
    return webClientUtils.get("/api/v1/Users/1", FakeUser::class)
      .log()
  }
}

data class FakeUser(
  val id: Int,
  val username: String,
  val password: String
)