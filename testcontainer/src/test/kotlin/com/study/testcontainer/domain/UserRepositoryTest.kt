package com.study.testcontainer.domain

import com.study.testcontainer.base.RepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired

@RepositoryTest
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
) {

//  companion object {
//    @Container
//    @ServiceConnection
//    private val MYSQL_CONTAINER = MySQLContainer(DockerImageName.parse("mysql:8.4.4"))
//  }

  @Test
  fun test1() {
    val user = assertDoesNotThrow { userRepository.save(User.of("test")) }
    assertThat(user.id).isNotNull
  }

  @Test
  fun test2() {
    val user = assertDoesNotThrow { userRepository.save(User.of("test")) }
    assertThat(user.id).isNotNull
  }
}
