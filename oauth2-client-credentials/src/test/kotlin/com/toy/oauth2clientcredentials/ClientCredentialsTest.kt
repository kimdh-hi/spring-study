package com.toy.oauth2clientcredentials

import com.jayway.jsonpath.JsonPath
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
import java.util.Base64

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

  data class Client(val id: String, val secret: String)
}
