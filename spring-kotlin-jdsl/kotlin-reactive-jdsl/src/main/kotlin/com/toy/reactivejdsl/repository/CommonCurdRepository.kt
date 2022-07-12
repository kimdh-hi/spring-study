package com.toy.reactivejdsl.repository

import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

//todo, 리플렉션으로 공통의 crudRepository를 만들 수 있는지 ... ing ...

@Repository
class CommonCrudRepository<T, ID>(
  val sessionFactory: Mutiny.SessionFactory,
  val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
)

suspend inline fun <reified T : Any, reified ID> CommonCrudRepository<T, ID>.get(id: ID): T? {
  return queryFactory.singleQuery {
    select(entity(T::class))
    from(entity(T::class))
//    where(col(T::class.constructors))
  }
}