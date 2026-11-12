package com.toy.springmfa.support

import com.jayway.jsonpath.JsonPath
import com.toy.springmfa.auth.passkey.PasskeyCredentialRepository
import com.toy.springmfa.auth.passkey.PasskeyUserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig::class)
abstract class IntegrationTestSupport {

  @Autowired
  protected lateinit var mockMvc: MockMvc

  @Autowired
  protected lateinit var clock: MutableClock

  @Autowired
  private lateinit var passkeyCredentialRepository: PasskeyCredentialRepository

  @Autowired
  private lateinit var passkeyUserRepository: PasskeyUserRepository

  @BeforeEach
  fun reset() {
    clock.reset()
    passkeyCredentialRepository.deleteAllInBatch()
    passkeyUserRepository.deleteAllInBatch()
  }

  protected fun login(username: String, password: String = "password"): String =
    tokenOf(
      mockMvc.post("/auth/login") {
        contentType = MediaType.APPLICATION_JSON
        content = """{"username":"$username","password":"$password"}"""
      },
    )

  protected fun get(path: String, accessToken: String): ResultActionsDsl =
    mockMvc.get(path) { bearer(accessToken) }

  protected fun post(path: String, accessToken: String, body: String? = null): ResultActionsDsl =
    mockMvc.post(path) {
      bearer(accessToken)
      body?.let {
        contentType = MediaType.APPLICATION_JSON
        content = it
      }
    }

  protected fun tokenOf(result: ResultActionsDsl): String =
    JsonPath.read(
      result.andExpect { status { isOk() } }.andReturn().response.contentAsString,
      "$.accessToken",
    )

  private fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.bearer(accessToken: String) {
    header("Authorization", "Bearer $accessToken")
  }
}

@TestConfiguration
class TestConfig {

  @Bean
  @Primary
  fun mutableClock() = MutableClock(Instant.now())
}

class MutableClock(
  private var instant: Instant,
  private val zone: ZoneId = ZoneOffset.UTC,
) : Clock() {

  override fun instant(): Instant = instant

  override fun getZone(): ZoneId = zone

  override fun withZone(zone: ZoneId): Clock = MutableClock(instant, zone)

  fun advance(duration: Duration) {
    instant = instant.plus(duration)
  }

  fun reset() {
    instant = Instant.now()
  }
}
