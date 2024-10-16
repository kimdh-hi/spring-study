package com.toy.springspel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.expression.spel.standard.SpelExpressionParser

class ParserTest {

  @Test
  fun parseSingleValue() {
    val data = "value"
    val expression = "#data"
    val parseExpression = SpelExpressionParser().parseExpression(expression)
    val value  = parseExpression.value

    assertThat(value).isEqualTo(data)
  }
}
