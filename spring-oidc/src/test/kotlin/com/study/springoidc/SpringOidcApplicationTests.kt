package com.study.springoidc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SpringOidcApplicationTests {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `openid configuration 노출`() {
    mockMvc.perform(get("/.well-known/openid-configuration").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.issuer").value("http://localhost:9000"))
      .andExpect(jsonPath("$.userinfo_endpoint").exists())
  }

  @Test
  fun `resource server 는 토큰 없으면 401`() {
    mockMvc.perform(get("/api/me"))
      .andExpect(status().isUnauthorized)
  }
}
