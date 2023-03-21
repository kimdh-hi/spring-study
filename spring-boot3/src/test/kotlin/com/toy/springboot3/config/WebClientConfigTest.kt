package com.toy.springboot3.config

import com.toy.springboot3.common.NoArg
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@SpringBootTest
class WebClientConfigTest @Autowired constructor(
  private val webClient: WebClient,
  @Qualifier("otherWebClient") private val otherWebClient: WebClient
) {

  companion object {
    const val FAKE_REST_API_URI = "https://fakerestapi.azurewebsites.net/api/v1/Users"
  }

  @Test
  fun test() {
    val result = webClient.get()
      .uri("$FAKE_REST_API_URI/1")
      .retrieve()
      .bodyToMono<FakeUser>()
      .block()

    println(result)

    val result2 = otherWebClient.get()
      .uri("$FAKE_REST_API_URI/1")
      .retrieve()
      .bodyToMono<FakeUser>()
      .block()

    println(result2)
  }
}

@NoArg
data class FakeUser(
  val id: Int,
  val userName: String,
  val password: String
)