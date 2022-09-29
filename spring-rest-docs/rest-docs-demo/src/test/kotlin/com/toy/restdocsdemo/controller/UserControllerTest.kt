package com.toy.restdocsdemo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.restdocsdemo.base.AbstractBaseTest
import com.toy.restdocsdemo.vo.UserCreateRequestVO
import com.toy.restdocsdemo.vo.UserUpdateRequestVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.transaction.annotation.Transactional

internal class UserControllerTest: AbstractBaseTest() {

  @Test
  fun list() {
    //when
    val result = mockMvc.get("/api/users")
      .andDo { print() }

    //then
    result
      .andExpect { status { isOk() } }

  }

  @Test
  fun get() {
    //when
    val result = mockMvc.get("/api/users/{id}", 1)
      .andDo { print() }

    //then
    result
      .andExpect { status { isOk() } }
  }

  @Test
  fun create() {
    //given
    val requestVO = UserCreateRequestVO(email = "test@gmail.com", "test")

    //when
    val result = mockMvc.post("/api/users") {
      contentType = MediaType.APPLICATION_JSON
      content = ObjectMapper().writeValueAsString(requestVO)
    }
      .andDo { print() }

    //then
    result
      .andExpect { status { isOk() } }
  }

  @Test
  fun update() {
    //given
    val requestVO = UserUpdateRequestVO(name = "update-test")

    //when
    val result = mockMvc.put("/api/users/{id}", 1) {
      contentType = MediaType.APPLICATION_JSON
      content = ObjectMapper().writeValueAsString(requestVO)
    }
      .andDo { print() }

    //then
    result
      .andExpect { status { isNoContent() } }
  }
}