package com.toy.springcloudconfigtest

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.TestConstructor
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest(
  properties = [
    "spring.cloud.config.enabled=true",
    "management.endpoints.web.exposure.include=*",
    "encrypt.key=abcd1234"
  ]
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class Encrypt(
  private val restTemplate: RestTemplate
) {

  @Test
  fun encrypt() {
    val plainProperty = "plainPropertyTest"

    val responseBody = restTemplate.exchange(
      URI.create("http://localhost:8881/encrypt"), HttpMethod.POST, HttpEntity(plainProperty), String::class.java
    ).body

    println(responseBody)
  }
}