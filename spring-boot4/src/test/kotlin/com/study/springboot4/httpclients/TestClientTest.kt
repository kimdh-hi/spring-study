package com.study.springboot4.httpclients

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TestClientTest @Autowired constructor(
  private val testClient: TestClient,
) {

  @Test
  fun get() {
    val response = testClient.get(1)

    assertAll(
      { assertThat(response).isNotNull },
      { assertThat(response!!.userId).isEqualTo(1L) },
    )
  }
}
