package com.toy.dpopproofjwt

import com.jayway.jsonpath.JsonPath
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.toy.dpopproofjwt.infra.security.DPoPNonceProvider
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class DPoPTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var nonceProvider: DPoPNonceProvider

  private val clientKey: ECKey = newKey()

  @Test
  fun `crud with dpop bound token`() {
    val token = login()

    val id = JsonPath.read<Int>(
      mockMvc.post(ITEMS) {
        dPoP(token, "POST", ITEMS)
        contentType = MediaType.APPLICATION_JSON
        content = """{"name":"a"}"""
      }.andExpect { status { isCreated() } }.andReturn().response.contentAsString,
      "$.id",
    )

    mockMvc.put("$ITEMS/$id") {
      dPoP(token, "PUT", "$ITEMS/$id")
      contentType = MediaType.APPLICATION_JSON
      content = """{"name":"b"}"""
    }.andExpect { status { isOk() }; jsonPath("$.name") { value("b") } }

    mockMvc.get("$ITEMS/$id") { dPoP(token, "GET", "$ITEMS/$id") }
      .andExpect { status { isOk() } }

    mockMvc.delete("$ITEMS/$id") { dPoP(token, "DELETE", "$ITEMS/$id") }
      .andExpect { status { isNoContent() } }
  }

  @Test
  fun `unknown item returns not found`() {
    val token = login()

    mockMvc.get("$ITEMS/999") { dPoP(token, "GET", "$ITEMS/999") }
      .andExpect { status { isNotFound() }; jsonPath("$.error") { value("not_found") } }
  }

  @Test
  fun `login endpoint requires dpop proof`() {
    mockMvc.post(LOGIN) {
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_dpop_proof") } }
  }

  @Test
  fun `login endpoint rejects bad credentials`() {
    mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", LOGIN))
      contentType = MediaType.APPLICATION_JSON
      content = """{"username":"user","password":"wrong"}"""
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_grant") } }
  }

  @Test
  fun `login endpoint rejects multiple dpop headers`() {
    mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", LOGIN), proof(clientKey, "POST", LOGIN))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect {
      status { isBadRequest() }
      jsonPath("$.error") { value("invalid_dpop_proof") }
      jsonPath("$.error_description") { exists() }
    }
  }

  @Test
  fun `login endpoint rejects proof for another uri`() {
    mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", ITEMS))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect { status { isBadRequest() } }
  }

  @Test
  fun `dpop bound token cannot be downgraded to bearer`() {
    val token = login()

    mockMvc.get(ITEMS) { header("Authorization", "Bearer $token") }
      .andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `missing proof is rejected`() {
    val token = login()

    mockMvc.get(ITEMS) { header("Authorization", "DPoP $token") }
      .andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `proof signed by another key is rejected`() {
    val token = login()

    getItems(token, proof(newKey(), "GET", ITEMS, token)).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `proof with mismatched htm, htu or ath is rejected`() {
    val token = login()
    val otherToken = login()

    getItems(token, proof(clientKey, "POST", ITEMS, token)).andExpect { status { isUnauthorized() } }
    getItems(token, proof(clientKey, "GET", "$ITEMS/1", token)).andExpect { status { isUnauthorized() } }
    getItems(token, proof(clientKey, "GET", ITEMS, otherToken)).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `expired proof is rejected`() {
    val token = login()
    val issuedAt = Date(System.currentTimeMillis() - 120_000)

    getItems(token, proof(clientKey, "GET", ITEMS, token, issuedAt)).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `replayed proof is rejected`() {
    val token = login()
    val proof = proof(clientKey, "GET", ITEMS, token)

    getItems(token, proof).andExpect { status { isOk() } }
    getItems(token, proof).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `login without nonce is challenged and succeeds on retry`() {
    val nonce = mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", LOGIN, nonce = null))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect {
      status { isBadRequest() }
      jsonPath("$.error") { value("use_dpop_nonce") }
      header { exists("DPoP-Nonce") }
    }.andReturn().response.getHeader("DPoP-Nonce")

    mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", LOGIN, nonce = nonce))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect { status { isOk() }; header { exists("DPoP-Nonce") } }
  }

  @Test
  fun `resource request without nonce is challenged`() {
    val token = login()

    getItems(token, proof(clientKey, "GET", ITEMS, token, nonce = null)).andExpect {
      status { isUnauthorized() }
      header { string("WWW-Authenticate", containsString("use_dpop_nonce")) }
      header { exists("DPoP-Nonce") }
    }
  }

  @Test
  fun `expired or forged nonce is challenged`() {
    val expired = nonceProvider.issue(Instant.now().minus(Duration.ofMinutes(5)))
    val forged = DPoPNonceProvider().issue()

    listOf(expired, forged, "not-a-nonce").forEach { nonce ->
      mockMvc.post(LOGIN) {
        header("DPoP", proof(clientKey, "POST", LOGIN, nonce = nonce))
        contentType = MediaType.APPLICATION_JSON
        content = CREDENTIALS
      }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("use_dpop_nonce") } }
    }
  }

  @Test
  fun `invalid proof with missing nonce is reported as invalid proof`() {
    mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", ITEMS, nonce = null))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_dpop_proof") } }
  }

  private fun login(): String {
    val body = mockMvc.post(LOGIN) {
      header("DPoP", proof(clientKey, "POST", LOGIN))
      contentType = MediaType.APPLICATION_JSON
      content = CREDENTIALS
    }.andExpect {
      status { isOk() }
      jsonPath("$.token_type") { value("DPoP") }
      jsonPath("$.expires_in") { value(300) }
      header { string("Cache-Control", containsString("no-store")) }
    }.andReturn().response.contentAsString
    return JsonPath.read(body, "$.access_token")
  }

  private fun getItems(token: String, proof: String): ResultActionsDsl =
    mockMvc.get(ITEMS) {
      header("Authorization", "DPoP $token")
      header("DPoP", proof)
    }

  private fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.dPoP(token: String, method: String, path: String) {
    header("Authorization", "DPoP $token")
    header("DPoP", proof(clientKey, method, path, token))
  }

  private fun proof(
    key: ECKey,
    method: String,
    path: String,
    accessToken: String? = null,
    issuedAt: Date = Date(),
    nonce: String? = nonceProvider.issue(),
  ): String {
    val header = JWSHeader.Builder(JWSAlgorithm.ES256)
      .type(JOSEObjectType("dpop+jwt"))
      .jwk(key.toPublicJWK())
      .build()
    val claims = JWTClaimsSet.Builder()
      .jwtID(UUID.randomUUID().toString())
      .claim("htm", method)
      .claim("htu", BASE + path)
      .issueTime(issuedAt)
      .apply { accessToken?.let { claim("ath", sha256(it)) } }
      .apply { nonce?.let { claim("nonce", it) } }
      .build()
    return SignedJWT(header, claims).apply { sign(ECDSASigner(key)) }.serialize()
  }

  companion object {
    const val BASE = "http://localhost"
    const val LOGIN = "/login"
    const val ITEMS = "/api/items"
    const val CREDENTIALS = """{"username":"user","password":"password"}"""

    fun newKey(): ECKey = ECKeyGenerator(Curve.P_256).generate()

    private fun sha256(value: String): String =
      Base64.getUrlEncoder().withoutPadding()
        .encodeToString(MessageDigest.getInstance("SHA-256").digest(value.toByteArray()))
  }
}
