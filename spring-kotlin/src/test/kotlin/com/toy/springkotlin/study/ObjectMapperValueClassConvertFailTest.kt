package com.toy.springkotlin.study

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * private data class TargetInnerDto(
 *   @field:JsonProperty("addData")
 *   val additionalData: String,
 *   val data: TestStringValue
 * )
 *
 * ??
 * - @field 없는 경우 JsonProperty 적용 안 됨. (convertValue 시 실패)
 * - data 를 value class 가 아닌 String 타입으로 지정하는 경우 @field 없이 convertValue 성공
 */

@SpringBootTest
class ObjectMapperValueClassConvertFailTest @Autowired constructor(private val objectMapper: ObjectMapper) {

  @Test
  fun `string covert to string value class`() {
    val testDto5 = SourceDto(SourceInnerDto("addTest", "test"))
    assertDoesNotThrow {
      val testDto6 = objectMapper.convertValue<TargetDto>(testDto5)
      println(testDto6)
    }
  }
}

private data class SourceDto(
  val data: Any,
)

private data class SourceInnerDto(
  val addData: String,
  val data: String,
)

private data class TargetDto(
  val data: TargetInnerDto,
)

private data class TargetInnerDto(
  @field:JsonProperty("addData")
  val additionalData: String,
  val data: TestStringValue
)

@JvmInline
value class TestStringValue(val value: String)
