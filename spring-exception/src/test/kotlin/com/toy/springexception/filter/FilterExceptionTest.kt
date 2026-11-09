package com.toy.springexception.filter

import com.toy.springexception.exception.ErrorCodes
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilterExceptionTest(
  @LocalServerPort private val port: Int,
) {

  private val client: RestTestClient by lazy {
    RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
  }

  @Test
  fun `필터에서 known exception 발생`() {
    client.get().uri("/filters/known").exchange()
      .expectStatus().isBadRequest
      .expectBody()
      .jsonPath("$.errorCode").isEqualTo(ErrorCodes.DATA_NOT_FOUND.code)
  }

  @Test
  fun `필터에서 unknown exception 발생`() {
    client.get().uri("/filters/unknown").exchange()
      .expectStatus().is5xxServerError
      .expectBody()
      .jsonPath("$.errorCode").isEqualTo(ErrorCodes.UNKNOWN.code)
  }

  @Test
  fun `필터에서 sendError 호출`() {
    client.get().uri("/filters/send-error").exchange()
      .expectStatus().isUnauthorized
      .expectBody()
      .jsonPath("$.errorCode").isEqualTo(ErrorCodes.UNKNOWN.code)
  }
}
