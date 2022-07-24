package com.toy.kotlinjdsl.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserControllerTest(
  val mockMvc: MockMvc
) {

  @Test
  fun get() {
    //when
    val result = mockMvc.get("/api/users/user-01")
      .andDo { print() }

    //expect
    result.andExpect {
      status { isOk() }
    }
  }
}