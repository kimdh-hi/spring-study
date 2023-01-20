package com.example.kotest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringSpecTest : StringSpec({

  "0으로 나누면 예외가 발생한다" {
    shouldThrow<Exception> { 1/0 }
  }

  "1은 1이다" {
    val data = 1
    data.shouldBe(1)
  }
})