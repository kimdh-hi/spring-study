package com.toy.kotlinjdsl.repository

import com.toy.kotlinjdsl.vo.UserSearchVO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserQueryImplTest(val userRepository: UserRepository) {

  @Test
  fun findAll() {
    //given
    val pageable = PageRequest.of(0, 2, Sort.by(Direction.DESC, "name"))

    //when
    val page = userRepository.findAll(pageable)

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
  fun searchV0() {
    //given
    val pageable = PageRequest.of(0, 2, Sort.by(Direction.DESC, "name"))

    //when
    val users = userRepository.searchV0(pageable)
  }

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
    val page = userRepository.searchV2(pageable)

    //then
    println(page.content)
    assertAll({
      assertNotNull(page.content)
      assertEquals(page.number, 0)
      assertEquals(page.size, 2)
      assertEquals(page.totalElements, 6)
    })
  }

  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @Nested
  inner class SearchV3 {

    @ParameterizedTest
    @MethodSource("parametersForSearch")
    fun search(searchVO: UserSearchVO) {
      //given
      val pageable = PageRequest.of(0, 2, Sort.by(Direction.DESC, "name"))

      //when
      val page = userRepository.searchV3(searchVO, pageable)

      //then
      println(page.content)
      assertAll({
        assertNotNull(page.content)
        assertEquals(page.number, 0)
      })
    }

    private fun parametersForSearch() =
      listOf(
        Arguments.of(UserSearchVO(roleId = "role-01")),
        Arguments.of(UserSearchVO(roleId = "role-09")),
        Arguments.of(UserSearchVO(keyword = "kim")),
        Arguments.of(UserSearchVO(keyword = "im", roleId = "role-09")),
      )
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

  @Test
  fun getV0() {
    //when
    val user = userRepository.findByIdOrNull("user-01")

    //expect
    println(user)
    assertAll({
      assertNotNull(user!!.id)
      assertEquals("user-01", user.id)
    })
  }
}