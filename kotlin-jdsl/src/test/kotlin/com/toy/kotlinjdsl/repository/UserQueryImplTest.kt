package com.toy.kotlinjdsl.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserQueryImplTest(val userRepository: UserRepository) {

  @Test
  fun searchV1() {
    //given
    val pageable = PageRequest.of(0, 2, Sort.by(Direction.DESC, "name"))

    //when
    val page = userRepository.searchV1(pageable)

    //then
    println(page.content)
    assertAll({
      assertNotNull(page.content)
      assertEquals(page.number, 0)
      assertEquals(page.size, 2)
      assertEquals(page.totalElements, 6)
    })
  }

  @Test
  fun searchV2() {
    //given
    val pageable = PageRequest.of(0, 2, Sort.by(Direction.DESC, "name"))

    //when
    val page = userRepository.searchV1(pageable)

    //then
    println(page.content)
    assertAll({
      assertNotNull(page.content)
      assertEquals(page.number, 0)
      assertEquals(page.size, 2)
      assertEquals(page.totalElements, 6)
    })
  }

  @Test
  fun get() {
    //when
    val user = userRepository.get("user-01")

    //expect
    println(user)
    assertAll({
      assertNotNull(user!!.id)
      assertEquals("user-01", user.id)
    })
  }
}