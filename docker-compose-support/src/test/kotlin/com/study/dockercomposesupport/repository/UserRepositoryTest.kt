package com.study.dockercomposesupport.repository

import com.study.dockercomposesupport.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
) {

  @Test
  fun save() {
    val user = assertDoesNotThrow { userRepository.save(User.of("test")) }
    assertThat(user.id).isNotNull
  }

  @Test
  fun find() {
    val user = userRepository.save(User.of("test"))

    val findUser = userRepository.findByIdOrNull(user.id!!)
    assertThat(findUser).isNotNull
  }
}
