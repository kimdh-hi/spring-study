package com.toy.springquerydsl.`00-base`

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.domain.Member
import com.toy.springquerydsl.domain.Team
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TestBase {

  @Autowired
  lateinit var em: EntityManager
  lateinit var query: JPAQueryFactory

  @BeforeEach
  fun beforeEach() {
    query = JPAQueryFactory(em)
    val teamA = Team(name = "teamA")
    val teamB = Team(name = "teamB")
    em.persist(teamA)
    em.persist(teamB)

    val memeber1 = Member(username = "member1", age = 20, team = teamA)
    val memeber2 = Member(username = "member2", age = 21, team = teamA)
    val memeber3 = Member(username = "member3", age = 30, team = teamB)
    val memeber4 = Member(username = "member4", age = 31, team = teamB)
    em.persist(memeber1)
    em.persist(memeber2)
    em.persist(memeber3)
    em.persist(memeber4)

  }
}