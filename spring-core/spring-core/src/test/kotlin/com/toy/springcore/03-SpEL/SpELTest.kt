package com.toy.springcore.`03-SpEL`

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpELTest {

  @Value("#{'hello'}") lateinit var hello: String
  @Value("#{1 eq 1}") var oneEqOne: Boolean? = null
  @Value("#{1 ne 2}") var oneNotEqualTwo: Boolean? = null
  @Value("\${custom.number}") var number: Int? = null
  @Value("#{\${custom.number} eq 1}") var customNumberEq1: Boolean? = null

  @Test
  fun spELTest() {
    println(hello)
    println(oneEqOne)
    println(oneNotEqualTwo)
    println(number)
    println(customNumberEq1)
  }
}