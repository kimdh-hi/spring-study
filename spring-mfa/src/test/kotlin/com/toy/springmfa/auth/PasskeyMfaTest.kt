package com.toy.springmfa.auth

import com.toy.springmfa.support.IntegrationTestSupport
import com.toy.springmfa.support.VirtualPasskey
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import java.time.Duration

class PasskeyMfaTest : IntegrationTestSupport() {

  private val passkey = VirtualPasskey("http://localhost:8080")

  @Test
  fun `wrong password is rejected`() {
    mockMvc.post("/auth/login") {
      contentType = MediaType.APPLICATION_JSON
      content = """{"username":"admin","password":"wrong"}"""
    }.andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `admin api without passkey returns step-up challenge`() {
    val token = login("admin")

    get("/admin/me", token).andExpect {
      status { isUnauthorized() }
      header { string("WWW-Authenticate", containsString("insufficient_user_authentication")) }
      jsonPath("$.requiredFactors[0].authority") { value("FACTOR_WEBAUTHN") }
      jsonPath("$.requiredFactors[0].reason") { value("missing") }
    }
  }

  @Test
  fun `admin api is allowed after passkey step-up`() {
    val token = login("admin")
    registerPasskey(token)

    val mfaToken = stepUp(token)

    get("/admin/me", mfaToken).andExpect {
      status { isOk() }
      jsonPath("$.factors.FACTOR_PASSWORD") { exists() }
      jsonPath("$.factors.FACTOR_WEBAUTHN") { exists() }
    }
  }

  @Test
  fun `challenge is one-time`() {
    val token = login("admin")
    registerPasskey(token)
    val assertion = assertion(token)
    post("/mfa/passkeys/authentication", token, assertion).andExpect { status { isOk() } }

    post("/mfa/passkeys/authentication", token, assertion).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `passkey of another user is not merged`() {
    registerPasskey(login("admin"))
    val userToken = login("user")

    val adminAssertion = assertion(userToken)

    post("/mfa/passkeys/authentication", userToken, adminAssertion).andExpect {
      status { isUnauthorized() }
      jsonPath("$.detail") { value("factor owner mismatch") }
    }
  }

  @Test
  fun `passkey factor expires after valid duration`() {
    val token = login("admin")
    registerPasskey(token)
    val mfaToken = stepUp(token)

    clock.advance(Duration.ofMinutes(11))

    get("/admin/me", mfaToken).andExpect {
      status { isUnauthorized() }
      jsonPath("$.requiredFactors[0].authority") { value("FACTOR_WEBAUTHN") }
      jsonPath("$.requiredFactors[0].reason") { value("expired") }
    }
  }

  @Test
  fun `mfa does not grant admin role`() {
    val token = login("user")
    registerPasskey(token)

    get("/admin/me", stepUp(token)).andExpect { status { isForbidden() } }
  }

  private fun registerPasskey(token: String) {
    val options = post("/mfa/passkeys/registration/options", token)
      .andExpect { status { isOk() } }.andReturn().response.contentAsString
    post("/mfa/passkeys/registration", token, passkey.register(options, "test key"))
      .andExpect { status { isCreated() } }
  }

  private fun assertion(token: String): String {
    val options = post("/mfa/passkeys/authentication/options", token)
      .andExpect { status { isOk() } }.andReturn().response.contentAsString
    return passkey.authenticate(options)
  }

  private fun stepUp(token: String): String =
    tokenOf(post("/mfa/passkeys/authentication", token, assertion(token)))
}
