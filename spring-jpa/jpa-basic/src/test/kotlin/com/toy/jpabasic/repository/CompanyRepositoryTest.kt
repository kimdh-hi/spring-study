package com.toy.jpabasic.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CompanyRepositoryTest(
  private val companyRepository: CompanyRepository,
  private val userRepository: UserRepository,
  private val roleRepository: RoleRepository
) {

  @Test
  fun `delete cascade`() {
    //given
    val companyId = "comp-01"
    val userId = "user-01"
    val adminId = "admin-01"
    val user = userRepository.findByIdOrNull(userId)!!

    //when
    companyRepository.findByIdOrNull(companyId)?.let {
      companyRepository.delete(it)
    }

    //then
    assertAll({
      assertTrue(!userRepository.existsById(userId))
      assertTrue(!userRepository.existsById(adminId))
      assertTrue(roleRepository.existsById(user.role.id))
    })
  }
}