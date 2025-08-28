package com.toy.springtest.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.configureFor
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.marcinziolo.kotlin.wiremock.contains
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returns
import com.marcinziolo.kotlin.wiremock.returnsJson
import com.marcinziolo.kotlin.wiremock.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@SpringBootTest
class WireMockTest @Autowired constructor(
  private val restClient: RestClient
) {

  private lateinit var wiremock: WireMockServer

  @BeforeEach
  fun setup() {
    wiremock = WireMockServer(options().dynamicPort())
    wiremock.start()
    configureFor("localhost", wiremock.port())
  }

  @Test
  fun `basic sample`() {
    //given
    val response = """
        {
          "message":"test"
        }
        """.trimIndent()

    wiremock.get {
      url equalTo "/test"
    } returnsJson {
      statusCode = 200
      header = "Content-Type" to "application/json"
      body = response
    }

    //when
    val result = restClient.get().uri("http://localhost:${wiremock.port()}/test")
      .retrieve()
      .body<String>()

    //then
    assertAll(
      { assertThat(result).isEqualTo(response) },
      {
        wiremock.verify {
          urlPath equalTo "/test"
          atLeast = 1
        }
      }
    )
  }

  @AfterEach
  fun teardown() {
    wiremock.stop()
  }

}
