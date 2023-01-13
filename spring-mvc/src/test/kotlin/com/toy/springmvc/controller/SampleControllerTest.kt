package com.toy.springmvc.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class SampleControllerTest(
  private val mockMvc: MockMvc
) {

  @Test
  fun sample() {
    mockMvc.get("/sample/kim")
      .andDo { print() }
      .andExpect { content { string("sample kim") } }
  }

  @Test
  fun sample2() {
    mockMvc.get("/sample") {
      param("name", "kim")
    }
      .andDo { print() }
      .andExpect { content { string("sample kim") } }
  }
}