package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.EnumCollectionTestEntity
import com.toy.jpabasic.domain.Week
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.DayOfWeek
import java.util.EnumSet

@DataJpaTest
class EnumCollectionTestEntityRepositoryTest @Autowired constructor(
  private val enumCollectionTestEntityRepository: EnumCollectionTestEntityRepository,
  private val testEntityManager: TestEntityManager
) {

  @Test
  fun test() {
    val weeks = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
    val enumCollectionTestEntity = enumCollectionTestEntityRepository.save(EnumCollectionTestEntity(weeks = weeks))
    testEntityManager.flush()
    testEntityManager.clear()

    println(enumCollectionTestEntity)
  }
}