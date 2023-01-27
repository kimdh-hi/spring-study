package com.example.kotest.sample

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class `04-FunSpec`: FunSpec({

  test("test 입니다") {
    //given
    val data = 1

    //when
    val result = data == 1

    //then
    result.shouldBe(true)
  }
})