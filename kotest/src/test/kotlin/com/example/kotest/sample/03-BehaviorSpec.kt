package com.example.kotest.sample

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class BehaviorSpecTest : BehaviorSpec({

  Given("a와 b가 주어질 때") {
    val a = 1
    val b = 1

    When("a 와 b 를 더하면") {
      val result = a + b

      Then("2 다") {
        result.shouldBe(2)
      }

      Then("1보다 크다") {
        result.should { it > 1 }
      }
    }
  }
})