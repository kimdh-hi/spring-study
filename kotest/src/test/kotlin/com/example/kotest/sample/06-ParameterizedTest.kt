package com.example.kotest.sample

import com.example.kotest.InputVO
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class `06-ParameterizedTest`(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper
): BehaviorSpec({

  given("test input api를 호출하는 경우") {
    lateinit var result: ResultActionsDsl
    `when`("잘못된 값이 전달되는 경우") {
      forAll(
        row(InputVO(data = " ", email = " ")),
        row(InputVO(data = "asd", email = "asd")),
        row(InputVO(data = "asd", email = "asd@gmail.com")),
      ) {
        result = mockMvc.post("/test/input") {
          contentType = MediaType.APPLICATION_JSON
          content = objectMapper.writeValueAsString(it)
        }
          .andDo { print() }
      }
      then("예외가 발생한다") {
        result.andExpect {
          status { is5xxServerError() }
        }
      }
    }
  }
})