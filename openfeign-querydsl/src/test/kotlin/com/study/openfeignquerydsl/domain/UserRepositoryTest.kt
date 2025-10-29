package com.study.openfeignquerydsl.domain

import com.study.openfeignquerydsl.config.QueryDslConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(value = [QueryDslConfig::class])
@AutoConfigureTestDatabase(
  connection = EmbeddedDatabaseConnection.NONE,
  replace = AutoConfigureTestDatabase.Replace.NONE
)
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
) {

  @Test
  fun findAllCustom() {
    val team = teamRepository.save(Team(name = "teamA"))
    userRepository.save(User.of("name", Email("password"), team))

    assertDoesNotThrow { userRepository.findAllCustom() }
  }
}
