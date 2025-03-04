package com.toy.springjunitdemo.controller

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserControllerNestedTest(
  private val mockMvc: MockMvc
) {

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteTest {
    @ParameterizedTest
    @MethodSource("parametersByDelete")
    fun delete(id: Long) {
      mockMvc.delete("/api/users/$id")
        .andDo { print() }
        .andExpect { status { isOk() } }
    }

    private fun parametersByDelete() = listOf(
      Arguments.of(1)
    )
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTest {
    @Test
    fun get() {
      mockMvc.get("/api/users/1")
        .andDo { print() }
        .andExpect { status { isOk() } }
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class ListTest {
    @Test
    fun list() {
      mockMvc.get("/api/users")
        .andDo { print() }
        .andExpect { status { isOk() } }
    }
  }
}