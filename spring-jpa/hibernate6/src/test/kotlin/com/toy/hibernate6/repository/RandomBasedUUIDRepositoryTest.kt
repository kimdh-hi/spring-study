package com.toy.hibernate6.repository

import com.toy.hibernate6.domain.RandomBasedUUID
import com.toy.hibernate6.domain.TimeBasedUUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class RandomBasedUUIDRepositoryTest @Autowired constructor(
  private val randomBasedUUIDRepository: RandomBasedUUIDRepository
) {

  @Test
  fun test() {
    //given
    (1..10).forEach { data ->
      RandomBasedUUID(data = data.toString()).also { randomBasedUUIDRepository.save(it) }
    }

    //when
    randomBasedUUIDRepository.findAllByOrderByIdDesc().forEach { println(it) }
  }
}