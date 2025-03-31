package com.toy.springspel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import kotlin.test.Test

class SpelExpressionParserTest {

  @Test
  fun `parse multi arguments`() {
    val expressionParser = SpelExpressionParser()
    val expression = expressionParser.parseExpression("#data1 + '-' + #data2")

    val evaluationContext = StandardEvaluationContext()
    evaluationContext.setVariable("data1", "value1")
    evaluationContext.setVariable("data2", "value2")

    val value = expression.getValue(evaluationContext, String::class.java)
    assertThat(value).isEqualTo("value1-value2")
  }

  @Test
  fun bytesToString() {
    val originString = "hello"
    val bytes = originString.toByteArray()
    val expression = SpelExpressionParser().parseExpression("new String(#data)")

    val evaluationContext = StandardEvaluationContext()
    evaluationContext.setVariable("data", bytes)

    val value = expression.getValue(evaluationContext, String::class.java)
    assertEquals(originString, value)
  }
}
