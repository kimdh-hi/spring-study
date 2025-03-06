package com.toy.springkotlin.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.toy.springkotlin.controller.dto.CompanyId
import com.toy.springkotlin.controller.dto.UserId
import com.toy.springkotlin.controller.dto.UserSaveRequest
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
  fun save() {
    val request = UserSaveRequest(UserId("userId"), CompanyId("companyId"))
    val result = mockmvc.post("/users") {
      contentType = MediaType.APPLICATION_JSON
      content = jacksonObjectMapper().writeValueAsString(request)
    }

    result.andExpectAll {
      status { isOk() }
    }
  }
}
