package com.toy.reactivejdsl.repository

import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.toy.reactivejdsl.domain.BaseEntity
import org.springframework.stereotype.Repository
import javax.persistence.Id
import kotlin.reflect.full.memberProperties

//todo, 리플렉션으로 공통의 query 를 만들 수 있는지
@Repository
class CommonQuery<T, ID>(
  val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
)

suspend inline fun <reified T : BaseEntity, reified ID> CommonQuery<T, ID>.get(id: ID): T? {

  return queryFactory.singleQuery {
    select(entity(T::class))
    from(entity(T::class))
//    where(col(idProperty).equal(id.toString()))
  }
}