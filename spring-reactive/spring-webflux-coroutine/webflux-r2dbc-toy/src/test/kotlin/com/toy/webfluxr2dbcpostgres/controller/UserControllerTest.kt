package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.base.AbstractIntegrationTest
import com.toy.webfluxr2dbcpostgres.base.TestData
import com.toy.webfluxr2dbcpostgres.vo.UserSaveRequestVO
import com.toy.webfluxr2dbcpostgres.vo.UserSaveResponseVO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.reactive.function.BodyInserters
import reactor.blockhound.BlockHound
import java.io.FilterInputStream

internal class UserControllerTest: AbstractIntegrationTest() {

//  @BeforeAll
  fun beforeAll() {
    BlockHound.builder()
      .allowBlockingCallsInside("java.util.UUID", "randomUUID")
      .allowBlockingCallsInside(FilterInputStream::class.java.name, "read")
      .install()
  }

  @Disabled
  @Test
  fun `save`() = runBlocking {
    //given
    val requestVO = UserSaveRequestVO(name = "test-name", username = "test-username", password = "test1234")
    val token = jwtUtil.createToken(TestData.USER)

    //expect
    webTestClient.post()
      .uri("/api/users")
      .header(HttpHeaders.AUTHORIZATION, token)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(requestVO))
      .exchange()
      .expectStatus().isOk
      .expectBody(UserSaveResponseVO::class.java)
  }

  @Test
  fun `saveFail`() = runTest {
    //given
    val requestVO = UserSaveRequestVO(name = " ", username = "test-username", password = "test1234")
    val token = jwtUtil.createToken(TestData.USER)

    //expect
    webTestClient.post()
      .uri("/api/users")
      .header(HttpHeaders.AUTHORIZATION, token)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(requestVO))
      .exchange()
      .expectStatus().is4xxClientError
  }

  @Disabled
  @Test
  fun `get`() {
    //given
    val token = jwtUtil.createToken(TestData.USER)

    //expect
    webTestClient.get()
      .uri("/api/users")
      .header(HttpHeaders.AUTHORIZATION, token)
      .exchange()
      .expectStatus().isOk
  }
}