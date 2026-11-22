package com.toy.oauth2clientcredentials

import com.jayway.jsonpath.JsonPath
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.sun.net.httpserver.HttpServer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.net.InetSocketAddress
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class ClientCredentialsTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `client can call only apis granted by its scopes`() {
    val admin = adminToken()
    val client = createClient(admin, "service-a")
    val token = clientToken(client, "service-a")

    mockMvc.get("/api/service-a") { bearer(token) }
      .andExpect { status { isOk() }; jsonPath("$.clientId") { value(client.id) } }
    mockMvc.get("/api/service-b") { bearer(token) }
      .andExpect { status { isForbidden() } }
  }

  @Test
  fun `client with multiple scopes requests subset`() {
    val admin = adminToken()
    val client = createClient(admin, "service-a", "service-b")
    val token = clientToken(client, "service-b")

    mockMvc.get("/api/service-b") { bearer(token) }.andExpect { status { isOk() } }
    mockMvc.get("/api/service-a") { bearer(token) }.andExpect { status { isForbidden() } }
  }

  @Test
  fun `requesting unregistered scope is rejected`() {
    val client = createClient(adminToken(), "service-a")

    mockMvc.post("/oauth2/token") {
      basicAuth(client)
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=service-b"
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_scope") } }
  }

  @Test
  fun `client authenticates with private_key_jwt using registered public key`() {
    val admin = adminToken()
    val key = RSAKeyGenerator(2048).keyID("k1").generate()
    val pem = "-----BEGIN PUBLIC KEY-----\n" +
      Base64.getEncoder().encodeToString(key.toRSAPublicKey().encoded) +
      "\n-----END PUBLIC KEY-----"
    val clientId = registerClient(admin, """"authMethod":"PRIVATE_KEY_JWT","publicKey":${jsonString(pem)}""")

    val token = assertionToken(clientId, key)
    mockMvc.get("/api/service-a") { bearer(token) }.andExpect { status { isOk() } }

    assertionTokenRequest(clientId, RSAKeyGenerator(2048).keyID("k1").generate())
      .andExpect { status { isUnauthorized() }; jsonPath("$.error") { value("invalid_client") } }
  }

  @Test
  fun `client authenticates with private_key_jwt using jwks url`() {
    val admin = adminToken()
    val key = RSAKeyGenerator(2048).keyID("k1").generate()
    val jwks = JWKSet(key.toPublicJWK()).toString().toByteArray()
    val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
      createContext("/jwks") { exchange ->
        exchange.responseHeaders.add("Content-Type", "application/json")
        exchange.sendResponseHeaders(200, jwks.size.toLong())
        exchange.responseBody.use { it.write(jwks) }
      }
      start()
    }
    try {
      val url = "http://127.0.0.1:${server.address.port}/jwks"
      val clientId = registerClient(admin, """"authMethod":"PRIVATE_KEY_JWT","jwkSetUrl":"$url"""")

      val token = assertionToken(clientId, key)
      mockMvc.get("/api/service-a") { bearer(token) }.andExpect { status { isOk() } }
    } finally {
      server.stop(0)
    }
  }

  @Test
  fun `private_key_jwt requires exactly one of public key or jwks url`() {
    val admin = adminToken()
    listOf(
      """"authMethod":"PRIVATE_KEY_JWT"""",
      """"authMethod":"PRIVATE_KEY_JWT","publicKey":"x","jwkSetUrl":"https://a/jwks"""",
    ).forEach { fields ->
      mockMvc.post("/admin/clients") {
        bearer(admin)
        contentType = MediaType.APPLICATION_JSON
        content = """{"name":"c","scopes":["service-a"],$fields}"""
      }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_client_metadata") } }
    }
  }

  @Test
  fun `private_key_jwt client cannot use secret`() {
    val key = RSAKeyGenerator(2048).generate()
    val pem = Base64.getEncoder().encodeToString(key.toRSAPublicKey().encoded)
    val clientId = registerClient(adminToken(), """"authMethod":"PRIVATE_KEY_JWT","publicKey":"$pem"""")

    mockMvc.post("/oauth2/token") {
      basicAuth(Client(clientId, "anything"))
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=service-a"
    }.andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `invalid public key cannot be registered`() {
    mockMvc.post("/admin/clients") {
      bearer(adminToken())
      contentType = MediaType.APPLICATION_JSON
      content = """{"name":"c","scopes":["service-a"],"authMethod":"PRIVATE_KEY_JWT","publicKey":"nope"}"""
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_client_metadata") } }
  }

  @Test
  fun `wrong secret is rejected`() {
    val client = createClient(adminToken(), "service-a")

    mockMvc.post("/oauth2/token") {
      basicAuth(client.copy(secret = "wrong"))
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=service-a"
    }.andExpect { status { isUnauthorized() }; jsonPath("$.error") { value("invalid_client") } }
  }

  @Test
  fun `deleted client cannot get new token and issued token is rejected`() {
    val admin = adminToken()
    val client = createClient(admin, "service-a")
    val issued = clientToken(client, "service-a")

    mockMvc.delete("/admin/clients/${client.id}") { bearer(admin) }
      .andExpect { status { isNoContent() } }

    mockMvc.post("/oauth2/token") {
      basicAuth(client)
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=service-a"
    }.andExpect { status { isUnauthorized() } }

    mockMvc.get("/api/service-a") { bearer(issued) }.andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `admin crud`() {
    val admin = adminToken()
    val client = createClient(admin, "service-a")

    mockMvc.get("/admin/clients/${client.id}") { bearer(admin) }
      .andExpect {
        status { isOk() }
        jsonPath("$.scopes[0]") { value("service-a") }
        jsonPath("$.clientSecret") { doesNotExist() }
      }
    mockMvc.get("/admin/clients") { bearer(admin) }.andExpect { status { isOk() } }
    mockMvc.delete("/admin/clients/${client.id}") { bearer(admin) }.andExpect { status { isNoContent() } }
    mockMvc.get("/admin/clients/${client.id}") { bearer(admin) }.andExpect { status { isNotFound() } }
  }

  @Test
  fun `unsupported scope cannot be registered`() {
    mockMvc.post("/admin/clients") {
      bearer(adminToken())
      contentType = MediaType.APPLICATION_JSON
      content = """{"name":"c","scopes":["unknown"]}"""
    }.andExpect { status { isBadRequest() }; jsonPath("$.error") { value("invalid_scope") } }
  }

  @Test
  fun `admin api and resource api are isolated`() {
    val admin = adminToken()
    val client = createClient(admin, "service-a")
    val token = clientToken(client, "service-a")

    mockMvc.get("/admin/clients") { bearer(token) }.andExpect { status { isUnauthorized() } }
    mockMvc.get("/api/service-a") { bearer(admin) }.andExpect { status { isUnauthorized() } }
    mockMvc.get("/admin/clients").andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `admin login fails with wrong password`() {
    mockMvc.post("/admin/login") {
      contentType = MediaType.APPLICATION_JSON
      content = """{"username":"admin","password":"wrong"}"""
    }.andExpect { status { isUnauthorized() } }
  }

  private fun adminToken(): String = JsonPath.read(
    mockMvc.post("/admin/login") {
      contentType = MediaType.APPLICATION_JSON
      content = """{"username":"admin","password":"password"}"""
    }.andExpect { status { isOk() } }.andReturn().response.contentAsString,
    "$.accessToken",
  )

  private fun createClient(adminToken: String, vararg scopes: String): Client {
    val body = mockMvc.post("/admin/clients") {
      bearer(adminToken)
      contentType = MediaType.APPLICATION_JSON
      content = """{"name":"partner","scopes":[${scopes.joinToString { "\"$it\"" }}]}"""
    }.andExpect { status { isCreated() } }.andReturn().response.contentAsString
    return Client(JsonPath.read(body, "$.client.clientId"), JsonPath.read(body, "$.clientSecret"))
  }

  private fun registerClient(adminToken: String, authFields: String): String {
    val body = mockMvc.post("/admin/clients") {
      bearer(adminToken)
      contentType = MediaType.APPLICATION_JSON
      content = """{"name":"partner","scopes":["service-a"],$authFields}"""
    }.andExpect { status { isCreated() }; jsonPath("$.clientSecret") { doesNotExist() } }
      .andReturn().response.contentAsString
    return JsonPath.read(body, "$.client.clientId")
  }

  private fun assertionTokenRequest(clientId: String, key: RSAKey) =
    mockMvc.post("/oauth2/token") {
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=service-a&client_id=$clientId" +
        "&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer" +
        "&client_assertion=${clientAssertion(clientId, key)}"
    }

  private fun assertionToken(clientId: String, key: RSAKey): String = JsonPath.read(
    assertionTokenRequest(clientId, key).andExpect { status { isOk() } }.andReturn().response.contentAsString,
    "$.access_token",
  )

  private fun clientAssertion(clientId: String, key: RSAKey): String {
    val claims = JWTClaimsSet.Builder()
      .issuer(clientId)
      .subject(clientId)
      .audience("http://localhost:8080/oauth2/token")
      .jwtID(UUID.randomUUID().toString())
      .expirationTime(Date.from(Instant.now().plusSeconds(60)))
      .build()
    return SignedJWT(JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.keyID).build(), claims)
      .apply { sign(RSASSASigner(key)) }
      .serialize()
  }

  private fun jsonString(value: String) = "\"" + value.replace("\n", "\\n") + "\""

  private fun clientToken(client: Client, scope: String): String = JsonPath.read(
    mockMvc.post("/oauth2/token") {
      basicAuth(client)
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      content = "grant_type=client_credentials&scope=$scope"
    }.andExpect { status { isOk() } }.andReturn().response.contentAsString,
    "$.access_token",
  )

  private fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.bearer(token: String) {
    header(HttpHeaders.AUTHORIZATION, "Bearer $token")
  }

  private fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.basicAuth(client: Client) {
    val encoded = Base64.getEncoder().encodeToString("${client.id}:${client.secret}".toByteArray())
    header(HttpHeaders.AUTHORIZATION, "Basic $encoded")
  }

  data class Client(val id: String, val secret: String?)
}
