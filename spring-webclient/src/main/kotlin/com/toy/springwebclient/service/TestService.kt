package com.toy.springwebclient.service

import com.toy.springwebclient.utils.WebClientUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/*
각 서비스 성격에 맞는 webClient 설정을 사용하기 위해 @Qualifier 로 webClient 를 지정한다.
 */
@Service
class TestService(
  @Qualifier("anotherWebClient") private val webClient: WebClient
) {

  val webClientUtils = WebClientUtils(webClient)

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