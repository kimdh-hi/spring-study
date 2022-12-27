package com.toy.springquerydsl.`06-distinct`

import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.DistinctTestDefaultEntity
import com.toy.springquerydsl.domain.DistinctTestNameHashCodeEntity
import com.toy.springquerydsl.domain.QDistinctTestDefaultEntity.distinctTestDefaultEntity
import com.toy.springquerydsl.domain.QDistinctTestNameHashCodeEntity.distinctTestNameHashCodeEntity
import org.junit.jupiter.api.Test

class Distinct: BaseTest() {

  @Test
  fun test1() {
    val e1 = DistinctTestDefaultEntity(name = "test1")
    val e2 = DistinctTestDefaultEntity(name = "test2")
    val e3 = DistinctTestDefaultEntity(name = "test3")
    val e4 = DistinctTestDefaultEntity(name = "test3")
    em.persist(e1)
    em.persist(e2)
    em.persist(e3)
    em.persist(e4)

    val result = query.selectDistinct(distinctTestDefaultEntity)
      .from(distinctTestDefaultEntity)
      .fetch()

    println(result)
  }

  @Test
  fun test2() {
    val e1 = DistinctTestNameHashCodeEntity(name = "test1")
    val e2 = DistinctTestNameHashCodeEntity(name = "test2")

    val e3 = DistinctTestNameHashCodeEntity(name = "test3")
    val e4 = DistinctTestNameHashCodeEntity(name = "test3")
    em.persist(e1)
    em.persist(e2)
    em.persist(e3)
    em.persist(e4)

    val result = query.selectDistinct(distinctTestNameHashCodeEntity)
      .from(distinctTestNameHashCodeEntity)
      .fetch()

    println(result)
  }

}