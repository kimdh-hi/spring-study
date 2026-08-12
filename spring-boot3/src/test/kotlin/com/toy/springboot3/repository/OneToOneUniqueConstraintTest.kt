package com.toy.springboot3.repository

import com.toy.migration.OwnerEntity
import com.toy.migration.TargetEntity
import com.toy.springboot3.config.JpaConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.hibernate.exception.ConstraintViolationException

@DataJpaTest
@Import(JpaConfig::class)
@EntityScan(basePackages = ["com.toy.springboot3", "com.toy.migration"])
class OneToOneUniqueConstraintTest @Autowired constructor(
  private val em: TestEntityManager,
) {

  @Test
  fun `같은 대상을 참조하는 OneToOne 2건 저장시 unique 제약 위반`() {
    val target = em.persistFlushFind(TargetEntity())

    em.persist(OwnerEntity(target = target))

    assertThrows<ConstraintViolationException> {
      em.persist(OwnerEntity(target = target))
      em.flush()
    }
  }
}
