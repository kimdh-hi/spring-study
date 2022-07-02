package com.toy.jpabasic.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RoleRepositoryTest(
  val roleRepository: RoleRepository
) {

  @Test
  fun test() {
    val role = roleRepository.findById("role-01")

    println(role)
  }
}