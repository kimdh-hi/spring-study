package com.toy.jpabulkinsert.repository

import com.toy.jpabulkinsert.common.QueryCountInspector
import com.toy.jpabulkinsert.entity.Team
import com.toy.jpabulkinsert.entity.User
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@SpringBootTest
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
) {

  @Test
  fun saveAll() {
    QueryCountInspector.reset()
    val teams = teamRepository.saveAll(listOf(Team(name = "team1"), Team(name = "team2")))
    val users = (1..50).map {
      if (it % 2 == 0) {
        User(name = "name$it", team = teams[0])
      } else {
        User(name = "name$it", team = teams[1])
      }
    }
    userRepository.saveAll(users)
    userRepository.flush()

    assertThat(QueryCountInspector.queryCount).isEqualTo(2)
  }

  @Test
  fun saveMulti() {
    QueryCountInspector.reset()
    val team1 = teamRepository.save(Team(name = "team1"))
    (1 until 20).map { userRepository.save(User(name = "name$it", team = team1)) }

    val team2 = teamRepository.save(Team(name = "team2"))
    (21 until 50).map { userRepository.save(User(name = "name$it", team = team2)) }

    userRepository.flush()

    assertThat(QueryCountInspector.queryCount).isEqualTo(2)
  }
}
