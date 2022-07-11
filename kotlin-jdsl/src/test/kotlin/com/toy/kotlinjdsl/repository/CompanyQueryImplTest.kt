package com.toy.kotlinjdsl.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class CompanyQueryImplTest(
  val companyRepository: CompanyRepository
) {

  @Test
  fun existsByName() {
    //given
    val name = "comp01"
    val notExistsName = "notExistsComp"

    //when
    val exists = companyRepository.existsByName(name)
    val notExists = companyRepository.existsByName(notExistsName)

    //then
    assertAll({
      assertTrue(exists)
      assertFalse(notExists)
    })
  }
}