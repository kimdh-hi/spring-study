package com.toy.springquerydsl.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.domain.BooleanFieldTestEntity
import com.toy.springquerydsl.domain.QBooleanFieldTestEntity
import com.toy.springquerydsl.domain.QBooleanFieldTestEntity.booleanFieldTestEntity
import org.springframework.data.repository.CrudRepository

interface BooleanFieldTestRepository: CrudRepository<BooleanFieldTestEntity, String> {
}

interface BooleanFieldTestRepositoryCustom {

  fun find(): BooleanFieldTestEntity?
}

class BooleanFieldTestRepositoryImpl(
  private val query: JPAQueryFactory
): BooleanFieldTestRepositoryCustom {

  override fun find(): BooleanFieldTestEntity? {
    return query.selectFrom(booleanFieldTestEntity)
      .where(booleanFieldTestEntity.isDefault.isTrue)
      .fetchOne()
  }

}