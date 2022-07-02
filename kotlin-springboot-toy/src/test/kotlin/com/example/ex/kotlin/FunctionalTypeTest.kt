package com.example.ex.kotlin

import org.junit.jupiter.api.Test

class FunctionalTypeTest {

  @Test
  fun `v1`() {
    val testClass = TestClassV1()
    val result = testClass.doSomething() {
      it + 1L
    }

    println(result)
  }

  class TestClassV1 {

    fun doSomething(testFunctionInterface: TestFunctionInterface): Long {
      return testFunctionInterface.plusOne(10)
    }
  }

  // 함수형 인터페이스 - fun interface
  fun interface TestFunctionInterface {
    fun plusOne(initValue: Int): Long
  }

  @Test
  fun `v2`() {
    val testClass = TestClassV2()
    val result = testClass.doSomething() {
      it + 1L
    }

    println(result)
  }

  // kotlin functional type - (함수형 인터페이스 대체)
  class TestClassV2 {
    fun doSomething(plusOne: (initValue: Int) -> Long): Long {
      return plusOne(10)
    }
  }
}