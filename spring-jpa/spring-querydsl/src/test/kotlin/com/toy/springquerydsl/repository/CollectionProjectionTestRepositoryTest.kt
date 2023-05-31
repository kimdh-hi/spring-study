package com.toy.springquerydsl.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class CollectionProjectionTestRepositoryTest @Autowired constructor(
  private val collectionProjectionParentRepository: CollectionProjectionParentRepository,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun testV0() {
    val pageable = PageRequest.of(0, 2)

    val result = collectionProjectionParentRepository.getAllV0(pageable)
    println(objectMapper.writeValueAsString(result))
  }

  @Test
  fun testV1() {
    val result = collectionProjectionParentRepository.getAllV1()
    println(objectMapper.writeValueAsString(result))
  }

  @Test
  fun testV2() {
    val result = collectionProjectionParentRepository.getAllV2()
    println(objectMapper.writeValueAsString(result))
  }

  @Test
  fun testV3() {
    val pageable = PageRequest.of(0, 2)

    val result = collectionProjectionParentRepository.getAllV3(pageable)
    println(objectMapper.writeValueAsString(result))
  }

  @Test
  fun testV4() {
    val pageable = PageRequest.of(0, 2)

    val result = collectionProjectionParentRepository.getAllV4(pageable)
    println(objectMapper.writeValueAsString(result))
  }

  @Test
  fun testV5() {
    val pageable = PageRequest.of(0, 2)

    val result = collectionProjectionParentRepository.getAllV5(pageable)
    println(objectMapper.writeValueAsString(result))
  }
}