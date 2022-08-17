package com.toy.springwebclient.service

import com.toy.springwebclient.common.WebClientConstants
import com.toy.springwebclient.utils.WebClientUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/*
각 서비스 성격에 맞는 webClient 설정을 사용하기 위해 @Qualifier 로 webClient 를 지정한다.

지정하지 않는다면 @Bean 이름이 지정되지 않은 기본 webClient 가 빈으로 등록된다.
 */
@Service
class TestAnotherService(
  @Qualifier(WebClientConstants.ANOTHER) private val webClient: WebClient
) {

  private val webClientUtils = WebClientUtils(webClient)

  fun test(): Mono<FakeUser> {
    return webClientUtils.get("/api/v1/Users/1", FakeUser::class)
      .log()
  }
}