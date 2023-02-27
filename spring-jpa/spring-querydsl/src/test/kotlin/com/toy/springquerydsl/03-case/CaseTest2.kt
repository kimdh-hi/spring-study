package com.toy.springquerydsl.`03-case`

import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.CaseTestEntity1
import com.toy.springquerydsl.domain.CaseTestEntity2
import com.toy.springquerydsl.domain.CaseTestEntity3
import com.toy.springquerydsl.domain.CaseTestEntityType
import com.toy.springquerydsl.domain.QCaseTestEntity1.caseTestEntity1
import com.toy.springquerydsl.domain.QCaseTestEntity2.caseTestEntity2
import com.toy.springquerydsl.domain.QCaseTestEntity3.caseTestEntity3
import com.toy.springquerydsl.domain.QCaseTestResponseVO
import com.toy.springquerydsl.repository.CaseTestEntity1Repository
import com.toy.springquerydsl.repository.CaseTestEntity2Repository
import com.toy.springquerydsl.repository.CaseTestEntity3Repository
import org.junit.jupiter.api.Test

class CaseTest2(
  private val caseTestEntity1Repository: CaseTestEntity1Repository,
  private val caseTestEntity2Repository: CaseTestEntity2Repository,
  private val caseTestEntity3Repository: CaseTestEntity3Repository
): BaseTest() {

  @Test
  fun test() {
    val entity22 = caseTestEntity2Repository.save(CaseTestEntity2(data = "dd2"))
    val entity33 = caseTestEntity3Repository.save(CaseTestEntity3(data = "dd3"))
    val entity1 = caseTestEntity1Repository.save(CaseTestEntity1(data = "d1", otherCaseTestEntityId = entity22.id!!, caseTestEntityType = CaseTestEntityType.TYPE2))
    val entity2 = caseTestEntity1Repository.save(CaseTestEntity1(data = "d2", otherCaseTestEntityId = entity33.id!!, caseTestEntityType = CaseTestEntityType.TYPE3))

    val result = query.select(
      QCaseTestResponseVO(
        caseTestEntity1.id, caseTestEntity1.caseTestEntityType,
        CaseBuilder()
          .`when`(caseTestEntity1.caseTestEntityType.eq(CaseTestEntityType.TYPE2)).then(
            JPAExpressions.select(caseTestEntity2.data)
              .from(caseTestEntity2)
              .where(caseTestEntity2.id.eq(caseTestEntity1.otherCaseTestEntityId))
          )
          .`when`(caseTestEntity1.caseTestEntityType.eq(CaseTestEntityType.TYPE3)).then(
            JPAExpressions.select(caseTestEntity3.data)
              .from(caseTestEntity3)
              .where(caseTestEntity3.id.eq(caseTestEntity1.otherCaseTestEntityId))
          )
          .otherwise(Expressions.nullExpression())
      )
    )
      .from(caseTestEntity1)
      .fetch()

    println(result)
  }
}