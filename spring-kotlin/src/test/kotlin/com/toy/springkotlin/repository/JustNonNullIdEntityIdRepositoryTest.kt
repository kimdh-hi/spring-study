package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.JustNonNullIdEntity
import com.toy.springkotlin.entity.JustNonNullIdEntityRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class JustNonNullIdEntityIdRepositoryTest @Autowired constructor(
  private val justNonNullIdEntityRepository: JustNonNullIdEntityRepository,
) {

  @Test
  fun save() {
    assertThrows<RuntimeException> { justNonNullIdEntityRepository.save((JustNonNullIdEntity.of("data"))) }
  }
}
