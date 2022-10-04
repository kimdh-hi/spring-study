package com.lecture.inflearndatajpa.repository

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GenericRepository(
  private val genericRepository: GenericRepository
) {


}