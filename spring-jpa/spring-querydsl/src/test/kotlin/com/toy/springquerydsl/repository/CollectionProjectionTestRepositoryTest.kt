package com.toy.springquerydsl.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CollectionProjectionTestRepositoryTest @Autowired constructor(
  private val collectionProjectionParentRepository: CollectionProjectionParentRepository
) {

  @Test
  fun test() {
    val result = collectionProjectionParentRepository.getAll()
    println(result)
  }
}