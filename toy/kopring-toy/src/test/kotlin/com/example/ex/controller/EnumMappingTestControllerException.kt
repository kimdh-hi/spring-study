package com.example.ex.controller

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class EnumMappingTestControllerException(val mockMvc: MockMvc) {

  @Test
  fun `success`() {
    //given
    val request = """
      {
          "username": "user",
          "roleCode": "role-01"
      }
    """.trimIndent()

    //when
    mockMvc.post("/enum") {
      contentType = MediaType.APPLICATION_JSON
      content = request
    }
      .andDo { print() }
      .andExpect {
        status { isOk() }
      }
  }

  @Test
  fun `존재하지 않는 roleId`() {
    //given
    val request = """
      {
          "username": "user",
          "roleCode": "invalid ..."
      }
    """.trimIndent()

    //when
    mockMvc.post("/enum") {
      contentType = MediaType.APPLICATION_JSON
      content = request
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.code") { value("9000") }
      }
  }

  @Test
  fun `roleCode null`() {
    //given
    val request = """
      {
          "username": "user"         
      }
    """.trimIndent()

    //when
    mockMvc.post("/enum") {
      contentType = MediaType.APPLICATION_JSON
      content = request
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.code") { value("9000") }
      }
  }

}