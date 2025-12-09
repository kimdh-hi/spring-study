package com.study.springboot4.jackson

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import java.time.Instant
import java.time.LocalDateTime

@SpringBootTest
class JacksonTest @Autowired constructor(
  private val jsonMapper: JsonMapper,
  private val objectMapper: ObjectMapper,
) {

  @Test
  fun test() {
    val data = TestData("data", LocalDateTime.now(), Instant.now())
    val json1 = jsonMapper.writeValueAsString(data)
    println(json1)

    val json2 = objectMapper.writeValueAsString(data)
    println(json2)

    val data1 = jsonMapper.readValue(json1, TestData::class.java)
    println(data1)

    val data2 = objectMapper.readValue(json1, TestData::class.java)
    println(data2)
  }
}

private data class TestData(
  val data: String,
  val date: LocalDateTime,
  val instantDate: Instant,
)
