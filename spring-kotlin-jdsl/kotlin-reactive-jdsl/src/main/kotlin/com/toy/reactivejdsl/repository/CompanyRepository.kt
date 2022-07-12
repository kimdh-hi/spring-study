package com.toy.reactivejdsl.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.toy.reactivejdsl.domain.Company
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

interface CompanyRepository {
  suspend fun findById(id: String): Company?
  suspend fun save(company: Company): Company
}

@Repository
class CompanyRepositoryImpl (
  private val sessionFactory: Mutiny.SessionFactory,
  private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
): CompanyRepository {

  override suspend fun save(company: Company): Company {
    return company.also {
      sessionFactory.withSession { session ->
        session.persist(it).flatMap { session.flush() }
      }.awaitSuspending()
    }
  }

  override suspend fun findById(id: String): Company? {
    return queryFactory.singleQuery {
      select(entity(Company::class))
      from(entity(Company::class))
      where(col(Company::id).equal(id))
    }
  }
}