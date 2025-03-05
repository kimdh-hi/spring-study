package com.toy.springtest.dto

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

//ref: https://tech.kakaopay.com/post/katfun-joy-kotlin/
//copy() 파라미터 지정 방식 - 코드 중복 최소화, 명확한 테스트 대상
class UserRequestTest {

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UserRequestTestV2 {

    private val validUserRequest = UserRequest("kim", 30)

    @Test
    fun `age가 0보다 큰 경우 성공한다`() {
      assertDoesNotThrow { validUserRequest.copy(age = 30) }
    }

    @Test
    fun `age는 0보다 커야 한다`() {
      assertThrows<IllegalArgumentException> { validUserRequest.copy(age = 0) }
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UserRequestTestV1 {
    @Test
    fun `age가 0보다 큰 경우 성공한다`() {
      assertDoesNotThrow { UserRequest(name = "kim", age = 30) }
    }

    @Test
    fun `age는 0보다 커야 한다`() {
      assertThrows<IllegalArgumentException> { UserRequest(name = "kim", age = 0) }
    }
  }
}
