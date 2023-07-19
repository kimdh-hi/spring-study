package com.toy.springmvc.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PathVariableTestControllerTest(private val mockMvc: MockMvc) {

  @Test
  fun test1() {
    mockMvc.post("/api/path-variable/test1/1")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }

  @Test
  fun test2() {
    mockMvc.post("/api/path-variable/test1")
      .andDo { print() }
      .andExpect { status { isOk() } }
  }
}