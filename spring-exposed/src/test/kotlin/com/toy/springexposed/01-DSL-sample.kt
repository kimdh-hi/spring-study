package com.toy.springexposed

import com.toy.springexposed.domain.Member
import com.toy.springexposed.domain.Team
import com.toy.springexposed.domain.User
import com.toy.springexposed.domain.User.name
import com.toy.springexposed.vo.MemberResponseVO
import com.toy.springexposed.vo.UserResponseVO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import javax.sql.DataSource

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class `01-DSL-sample`(
  private val dataSource: DataSource
) {

  @Test
  fun sample1() {
    Database.connect(dataSource)

    transaction {
      addLogger(StdOutSqlLogger)
      SchemaUtils.create(User)

      User.insert {
        it[name] = "kim"
      }

      User
        .selectAll().where { name eq "kim" }
        .forEach { println(it) }

      User.update {
        run { name eq "kim" }
        run { it[name] = "kim-updated" }
      }

      val updateTestResult = User.selectAll().where { name eq "kim-updated" }
        .map { UserResponseVO.of(it) }
        .firstOrNull()
      println(updateTestResult)

      assertAll({
        assertNotNull(updateTestResult)
        assertEquals(updateTestResult!!.name, "kim-updated")
      })

      User
        .deleteWhere { name eq "kim-updated" }

      val deleteTestResult = User.selectAll().where { name eq "kim-updated" }.firstOrNull()
      assertNull(deleteTestResult)


      SchemaUtils.drop(User)
    }
  }

  @Test
  fun sample2() {
    Database.connect(dataSource)

    transaction {
      SchemaUtils.create(Team)
      SchemaUtils.create(Member)
      addLogger(StdOutSqlLogger)

      val teamId = Team.insert {
        it[name] = "team1"
      }[Team.id]

      val memberId = Member.insert {
        it[name] = "kim"
        it[team] = teamId
      }[Member.id]

      Member.selectAll()
        .forEach { println(it) }

      //join
      val memberResponseVO = (Member innerJoin Team)
        .slice(
          Team.id,
          Team.name,
          Member.id,
          Member.name,
        )
        .selectAll()
        .map { MemberResponseVO.of(it) }
        .firstOrNull()
      println(memberResponseVO)

      SchemaUtils.drop(Member)
      SchemaUtils.drop(Team)
    }
  }
}