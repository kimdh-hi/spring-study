package com.toy.springkotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.toy.springkotlin.controller.dto.CompanyId
import com.toy.springkotlin.controller.dto.UserName
import com.toy.springkotlin.controller.dto.UserSaveRequest
import com.toy.springkotlin.controller.dto.UserSaveV1Request
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT, printOnlyOnFailure = false)
class UserControllerTest @Autowired constructor(
  private val mockmvc: MockMvc,
) {

  @Test
  fun saveV2() {
    val request = UserSaveRequest(UserName("kim"), CompanyId("company-1"))
    val result = mockmvc.post("/users/v2") {
      contentType = MediaType.APPLICATION_JSON
      content = jacksonObjectMapper().writeValueAsString(request)
    }

    result.andExpectAll {
      status { isOk() }
    }
  }

  @Test
  fun saveV2Failed() {
    val request = UserSaveRequest(UserName(" "), CompanyId(" "))
    val result = mockmvc.post("/users/v2") {
      contentType = MediaType.APPLICATION_JSON
      content = jacksonObjectMapper().writeValueAsString(request)
    }

    result.andExpectAll {
      status { is4xxClientError() }
    }
  }

  @Test
  fun saveV1Failed() {
    val request = UserSaveV1Request(" ", " ")
    val result = mockmvc.post("/users/v1") {
      contentType = MediaType.APPLICATION_JSON
      content = jacksonObjectMapper().writeValueAsString(request)
    }

    result.andExpectAll {
      status { is4xxClientError() }
    }
  }
}
