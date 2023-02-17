package com.toy.springexposed

import com.toy.springexposed.domain.Member
import com.toy.springexposed.domain.Team
import com.toy.springexposed.domain.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import javax.sql.DataSource

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class `02-batch-insert`(
  private val dataSource: DataSource
) {

  @Test
  fun test() {

    Database.connect(dataSource)

    transaction {
      SchemaUtils.create(User)
      addLogger(StdOutSqlLogger)

      User.batchInsert(
        (1..100).map { it },
        ignore = false,
        shouldReturnGeneratedValues = false
      ) {
        this[User.name] = "$it-name"
      }

      SchemaUtils.drop(User)
    }
  }
}