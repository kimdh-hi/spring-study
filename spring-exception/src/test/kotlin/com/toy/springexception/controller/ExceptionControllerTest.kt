package com.toy.springexception.controller

import com.toy.springexception.exception.ErrorCodes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionControllerTest @Autowired constructor(
  private val mockmvc: MockMvc,
) {

  @Test
  fun unknownException() {
    mockmvc.get("/exceptions/unknown")
      .andExpectAll {
        status { is5xxServerError() }
        jsonPath("$.errorCode") { value(ErrorCodes.UNKNOWN.code) }
      }
  }

  @Test
  fun knownException() {
    mockmvc.get("/exceptions/known")
      .andExpectAll {
        status { is4xxClientError() }
        jsonPath("$.errorCode") { value(ErrorCodes.DATA_NOT_FOUND.code) }
      }
  }

  @Test
  fun `없는 api 호출`() {
    mockmvc.get("/not-exists")
      .andExpectAll {
        status { is4xxClientError() }
        jsonPath("$.errorCode") { value(ErrorCodes.API_NOT_FOUND.code) }
      }
  }
}
