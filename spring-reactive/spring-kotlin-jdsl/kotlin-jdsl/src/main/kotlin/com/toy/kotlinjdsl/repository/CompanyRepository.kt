package com.toy.kotlinjdsl.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.selectQuery
import com.toy.kotlinjdsl.domain.Company
import com.toy.kotlinjdsl.vo.ExistsVO
import org.hibernate.SessionFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface CompanyRepository: CrudRepository<Company, String>, CompanyQuery

interface CompanyQuery {
  fun existsByName(name: String): Boolean
}

@Repository
class CompanyQueryImpl(
  private val queryFactory: SpringDataQueryFactory): CompanyQuery {

  override fun existsByName(name: String): Boolean {
    val result = queryFactory.selectQuery<ExistsVO> {
      select(listOf(column(Company::id)))
      from(entity(Company::class))
      where(col(Company::name).equal(name))
      limit(1)
    }

    return result.resultList.isNotEmpty()
  }
}