package com.toy.springquerydsl.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.domain.Hello
import com.toy.springquerydsl.domain.QHello.hello
import org.springframework.data.jpa.repository.JpaRepository

interface HelloRepository: JpaRepository<Hello, String>, HelloRepositoryCustom

interface HelloRepositoryCustom {
  fun existsByDataUsingFetchOne(data: String): Boolean
  fun existsByDataUsingFetchFirst(data: String): Boolean
  fun existsByDataUsingFetchFirstZZZ(data: String): Int
}

class HelloRepositoryImpl(
  private val query: JPAQueryFactory
): HelloRepositoryCustom {
  override fun existsByDataUsingFetchOne(data: String): Boolean {
    val result = query.selectOne()
      .from(hello)
      .where(hello.data.eq(data))
      .fetchOne()

    return result != null
  }

  override fun existsByDataUsingFetchFirst(data: String): Boolean {
    val result =  query.selectOne()
      .from(hello)
      .where(hello.data.eq(data))
      .fetchFirst()

    return result != null
  }

  // fetchFirst 반환값을 non-nullable 타입으로 반환시 호출한 쪽에서 NPE 발생 가능
  override fun existsByDataUsingFetchFirstZZZ(data: String): Int {
    return query.selectOne()
      .from(hello)
      .where(hello.data.eq(data))
      .fetchFirst()!!
  }
}
