package com.toy.springswagger_restdocs.controller

import com.toy.springswagger_restdocs.base.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.post

class TestControllerTest: AbstractIntegrationTest() {

  @Test
  fun `save test`() {
    //given
    val dto = TestDto("data")

    //when
    val result = mockMvc.post("/api/test") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(dto)
    }

    //then
    result.andExpect {
      status { isOk() }
    }

    result.andDo {
      document("save",
        requestFields(
          fieldWithPath("data").type(JsonFieldType.STRING).description("데이터")
        )
      )
    }
  }

}