package com.study.jpacore.common

import org.hibernate.resource.jdbc.spi.StatementInspector
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(
  name = ["custom.use-query-counter"],
  havingValue = "true",
  matchIfMissing = false
)
class QueryCountInspector() : StatementInspector {

  override fun inspect(sql: String): String {
    QueryCounter.increase()
    return sql
  }
}
