package com.toy.springdataenvers.repository

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest(
  private val userRepository: UserRepository,
  private val userSomeData1Repository: UserSomeData1Repository,
  private val userSomeData2Repository: UserSomeData2Repository,
  private val userSomeData3Repository: UserSomeData3Repository
) {

  @Test
  fun test() {
    //given
    val userId = "u1"

    //when
    val user = userRepository.findByIdOrNull(userId)!!
    userRepository.delete(user)

    //then
    val result1 = userSomeData1Repository.findByUserId(userId)
    val result2 = userSomeData2Repository.findByUserId(userId)
    val result3 = userSomeData3Repository.findByUserId(userId)

    assertAll({
      assertNull(result1)
      assertNull(result2)
      assertNull(result3)
    })

//    assertAll({
//      assertNotNull(result1)
//      assertNotNull(result2)
//      assertNotNull(result3)
//    })
  }
}