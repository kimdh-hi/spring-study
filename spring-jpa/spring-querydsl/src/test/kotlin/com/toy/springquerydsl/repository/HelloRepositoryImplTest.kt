package com.toy.springquerydsl.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class HelloRepositoryImplTest @Autowired constructor(
  private val helloRepository: HelloRepository
) {

  @Test
  fun `fetchFirst - 2개 이상인 경우`() {
    val result = helloRepository.existsByDataUsingFetchFirst("data1")
    assertTrue(result)
  }

  @Test
  fun `fetchOne - 2개 이상인 경우`() {
    val result = helloRepository.existsByDataUsingFetchOne("data1")
    assertTrue(result)
  }

  @Test
  fun `fetchOne - 없는 경우`() {
    val result = helloRepository.existsByDataUsingFetchOne("none")
    println(result)
  }

  @Test
  fun `fetchFirst - 없는 경우`() {
    val result = helloRepository.existsByDataUsingFetchFirst("none")
    println(result)
  }
}
