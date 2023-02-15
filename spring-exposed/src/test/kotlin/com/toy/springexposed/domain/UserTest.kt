package com.toy.springexposed.domain

import com.toy.springexposed.domain.User.name
import com.toy.springexposed.vo.UserResponsVO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.util.UUID
import javax.sql.DataSource

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserTest(
  private val dataSource: DataSource
) {

  @Test
  fun sample1() {
    Database.connect(dataSource)

    transaction {
      SchemaUtils.create(User)

      User.insert {
        it[userId] = UUID.randomUUID().toString()
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
        .map { UserResponsVO.of(it) }
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