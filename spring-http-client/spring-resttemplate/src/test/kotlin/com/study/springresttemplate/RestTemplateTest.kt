package com.study.springresttemplate

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@SpringBootTest
class RestTemplateTest @Autowired constructor(
  private val restTemplate: RestTemplate,
) {

  private val logger = LoggerFactory.getLogger(RestTemplate::class.java)

  @Test
  fun request() {
    val result1 = restTemplate.getForEntity<TodoResponse>("https://jsonplaceholder.typicode.com/todos/1")
    val result2 = restTemplate.getForEntity<TodoResponse>("https://jsonplaceholder.typicode.com/todos/1")
    logger.info("result1: {}", result1)
    logger.info("result2: {}", result2)
  }
}

private data class TodoResponse(val userId: Int, val id: Int, val title: String, val completed: Boolean)
