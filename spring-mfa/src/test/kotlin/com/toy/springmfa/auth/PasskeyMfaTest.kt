package com.toy.springmfa.auth

import com.toy.springmfa.support.IntegrationTestSupport
import com.toy.springmfa.support.VirtualPasskey
import java.time.Duration
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class PasskeyMfaTest : IntegrationTestSupport() {

  private val passkey = VirtualPasskey("http://localhost:8080")

  @Test
  fun `wrong password is rejected`() {
    mockMvc.post("/login") {
      contentType = MediaType.APPLICATION_JSON
      content = """{"username":"user","password":"wrong"}"""
    }.andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `first login requires passkey registration`() {
    loginResult("user").andExpect {
      status { isOk() }
      jsonPath("$.nextStep") { value("PASSKEY_REGISTRATION") }
    }
  }

  @Test
  fun `login after registration requires passkey authentication`() {
    registerPasskey(login("user"))

    loginResult("user").andExpect {
      status { isOk() }
      jsonPath("$.nextStep") { value("PASSKEY_AUTHENTICATION") }
    }
  }

  @Test
  fun `password-only token cannot access api`() {
    val pendingToken = login("user")

    get("/me", pendingToken).andExpect {
      status { isUnauthorized() }
      header { string("WWW-Authenticate", containsString("insufficient_user_authentication")) }
      jsonPath("$.requiredFactors[0].authority") { value("FACTOR_WEBAUTHN") }
      jsonPath("$.requiredFactors[0].reason") { value("missing") }
    }
  }

  @Test
  fun `first login completes after registering and authenticating passkey`() {
    val pendingToken = login("user")
    registerPasskey(pendingToken)

    val accessToken = authenticatePasskey(pendingToken)

    get("/me", accessToken).andExpect {
      status { isOk() }
      jsonPath("$.factors.FACTOR_PASSWORD") { exists() }
      jsonPath("$.factors.FACTOR_WEBAUTHN") { exists() }
    }
  }

  @Test
  fun `next login completes with passkey authentication`() {
    registerPasskey(login("user"))
    val pendingToken = login("user")

    get("/me", authenticatePasskey(pendingToken)).andExpect { status { isOk() } }
  }

  @Test
  fun `password alone cannot register another passkey`() {
    registerPasskey(login("user"))
    val pendingToken = login("user")

    post("/mfa/passkeys/registration/options", pendingToken).andExpect { status { isForbidden() } }
  }

  @Test
  fun `additional passkey can be registered after mfa`() {
    registerPasskey(login("user"))
    val accessToken = authenticatePasskey(login("user"))

    registerPasskey(accessToken)
  }

  @Test
  fun `pending token expires`() {
    val pendingToken = login("user")

    clock.advance(Duration.ofMinutes(7))

    post("/mfa/passkeys/registration/options", pendingToken).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `challenge is one-time`() {
    val pendingToken = login("user")
    registerPasskey(pendingToken)
    val assertion = assertion(pendingToken)
    post("/mfa/passkeys/authentication", pendingToken, assertion).andExpect { status { isOk() } }

    post("/mfa/passkeys/authentication", pendingToken, assertion).andExpect { status { isUnauthorized() } }
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

  private fun authenticatePasskey(token: String): String =
    tokenOf(post("/mfa/passkeys/authentication", token, assertion(token)))
}
