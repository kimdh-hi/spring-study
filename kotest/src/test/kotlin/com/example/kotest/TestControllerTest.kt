package com.example.kotest

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TestControllerTest(
  private val mockMvc: MockMvc
): BehaviorSpec({

  Given("test api를 호출하는 경우") {
    val uri = "/test/{number}"

    When("1을 전달한다면") {
      val result = mockMvc.get(uri, 1)
        .andDo { print() }

      Then("성공한다") {
        result.andExpect {
          status { isOk() }
        }
      }
    }

    When("1이 아닌 숫자를 전달한다면") {
      val result = mockMvc.get(uri, 2)
        .andDo { print() }

      Then("실패한다") {
        result.andExpect {
          status { is5xxServerError() }
        }
      }
    }
  }
})