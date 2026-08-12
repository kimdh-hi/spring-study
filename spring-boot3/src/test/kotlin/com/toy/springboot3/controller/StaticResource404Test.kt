package com.toy.springboot3.controller

import com.toy.migration.CatchAllAdvice
import com.toy.migration.NoResourceAwareAdvice
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@Import(CatchAllAdvice::class)
class NoResourceFound500Test @Autowired constructor(
  private val mockMvc: MockMvc,
) {

  @Test
  fun `매핑 없는 경로가 advice 에 걸려 500`() {
    mockMvc.get("/no-such-path").andExpect {
      status { isInternalServerError() }
    }
  }
}

@WebMvcTest
@Import(NoResourceAwareAdvice::class)
class NoResourceFound404Test @Autowired constructor(
  private val mockMvc: MockMvc,
) {

  @Test
  fun `NoResourceFoundException 를 명시 처리하면 404`() {
    mockMvc.get("/no-such-path").andExpect {
      status { isNotFound() }
    }
  }
}
