package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.LockerRepository
import com.toy.jpabasic.domain.LockerUserRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class LockerUserRepositoryTest(
  private val lockerUserRepository: LockerUserRepository,
  private val lockerRepository: LockerRepository
) {

  @Test
  fun test1(){
    lockerRepository.findAll().forEach {
      println(it)
    }
  }

  @Test
  fun test2() {
    lockerUserRepository.findAll().forEach {
      println(it)
    }
  }
}