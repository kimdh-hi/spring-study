package com.toy.productinfo.repository

import com.toy.productinfo.domain.User
import com.toy.productinfo.domain.enum.UserRole
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@EnableJpaAuditing
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest(private val userRepository: UserRepository) {

  @Test
  fun `생성일자, 수정일자 자동생성` () {
    //given
    val user =
      User.newInstance(name = "name", username = "username", password = "password", role = UserRole.USER)

    //when
    val savedUser = userRepository.save(user)

    //then
    assertAll({
      assertNotNull(savedUser.createdDate)
      assertNotNull(savedUser.updatedDate)
    })
  }
}