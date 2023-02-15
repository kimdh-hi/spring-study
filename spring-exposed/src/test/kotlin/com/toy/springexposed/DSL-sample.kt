package com.toy.springexposed

import com.toy.springexposed.domain.User
import com.toy.springexposed.domain.User.name
import com.toy.springexposed.vo.UserResponseVO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import javax.sql.DataSource

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class `DSL-sample`(
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
        .select { name eq "kim" }
        .forEach { println(it) }

      User.update {
        run { name eq "kim" }
        run { it[name] = "kim-updated" }
      }

      val updateTestResult = User.select { name eq "kim-updated" }
        .map { UserResponseVO.of(it) }
        .firstOrNull()
      println(updateTestResult)
      assertAll({
        assertNotNull(updateTestResult)
        assertEquals(updateTestResult!!.name, "kim-updated")
      })

      User
        .deleteWhere { name eq "kim-updated" }

      val deleteTestResult = User.select { name eq "kim-updated" }.firstOrNull()
      assertNull(deleteTestResult)


      SchemaUtils.drop(User)
    }
  }
}