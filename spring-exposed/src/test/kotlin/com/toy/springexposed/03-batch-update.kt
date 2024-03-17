package com.toy.springexposed

import com.toy.springexposed.domain.User
import org.jetbrains.exposed.sql.statements.BatchUpdateStatement
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import javax.sql.DataSource

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class `03-batch-update`(
  private val dataSource: DataSource
) {

  @Test
  fun test() {
    BatchUpdateStatement(User).apply {
      
    }
  }
}