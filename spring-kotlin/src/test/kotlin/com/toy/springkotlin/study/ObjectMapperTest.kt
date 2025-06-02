package com.toy.springkotlin.study

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

/**
 * objectMapper 주입 사용
 * - com.fasterxml.jackson.module:jackson-module-kotlin 의존성 추가되어 있는 경우 Jackson2ObjectMapperBuilder.registerWellKnownModulesIfAvailable 에 의해 필요 모듈 등록
 *   - 별도 기본 생성자 정의 없이 직렬화 가능
 * - com.fasterxml.jackson.datatype:jackson-datatype-jsr310 모듈 별도 설정없이 등록되어 LocalDateTime 역직렬화시 이슈 없음
 *   - 위 모듈 적용되지 않는 경우 LocalDateTime 역직렬화시 리스트 포맷으로 잘못 역직렬화 됨.
 * - kotlin이 확장함수로 제공하는 convertValue, readValue 사용하려면 com.fasterxml.jackson.module:jackson-module-kotlin 의존성 추가 필요
 *
 *
 * jsonMapper { }
 * - com.fasterxml.jackson.module.kotlin 의존성 추가 필요
 * - objectMapper 생성하여 사용시 dsl 방식으로 설정 커스텀하여 생성 가능
 */


private val jsonMapper  = jsonMapper {
  addModule(kotlinModule())
  addModule(JavaTimeModule())
  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}

@SpringBootTest
class ObjectMapperTest @Autowired constructor(private val objectMapper: ObjectMapper) {

  private val json = """
    {
      "data": "data",
      "time": "2025-01-01T00:00:00Z"
    }
    """.trimIndent()

  @Test
  fun test1() {
    assertDoesNotThrow { objectMapper.readValue<TestDto>(json) }
  }

  @Test
  fun test2() {
    val json = assertDoesNotThrow { objectMapper.writeValueAsString(TestDto()) }
    println(json)
  }


  @Test
  fun test3() {
    val myObjectMapper = ObjectMapper()
    assertThrows<InvalidDefinitionException> { myObjectMapper.readValue<TestDto>(json) }
  }

  @Test
  fun test4() {
    val dto2 = TestDto2()

    // convertValue object -> object
    assertDoesNotThrow {
      objectMapper.convertValue<TestDto>(dto2)
    }

    // convertValue json string -> object
    assertDoesNotThrow {
      objectMapper.readValue<TestDto>(json)
    }
  }

  @Test
  fun test5() {
    val dto = TestDto()

    // convertValue object -> object
    assertDoesNotThrow {
      val resultJson = jsonMapper.convertValue<TestDto>(dto)
      println(resultJson)
    }

    // convertValue json string -> object
    assertDoesNotThrow {
      val readDto = jsonMapper.readValue<TestDto>(json)
      println(readDto)
    }
  }
}

private data class TestDto(
  val data: String = "data",
  val time: LocalDateTime = LocalDateTime.now(),
)

private data class TestDto2(
  val data: String = "data",
  val time: LocalDateTime = LocalDateTime.now(),
)

