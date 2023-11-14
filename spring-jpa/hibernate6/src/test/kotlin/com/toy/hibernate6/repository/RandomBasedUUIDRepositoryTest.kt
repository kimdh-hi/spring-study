package com.toy.hibernate6.repository

import com.toy.hibernate6.domain.TimeBasedUUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class TimeBasedUUIDRepositoryTest @Autowired constructor(
  private val timeBasedUUIDRepository: TimeBasedUUIDRepository
) {

  @Test
  fun test() {
    //given
    (1..10).forEach { data ->
      TimeBasedUUID(data = data.toString()).also { timeBasedUUIDRepository.save(it) }
    }

    //when
    timeBasedUUIDRepository.findAllByOrderByIdDesc().forEach { println(it) }
  }
}