package com.toy.reactivejdsl.common

import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Component

@Component
class MutinySessionHelper(
  private val sessionFactory: Mutiny.SessionFactory,
  private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
)
