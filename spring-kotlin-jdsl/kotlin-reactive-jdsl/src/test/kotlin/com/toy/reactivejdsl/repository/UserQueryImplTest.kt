package com.toy.reactivejdsl.repository

import com.toy.reactivejdsl.base.BaseTest
import com.toy.reactivejdsl.base.TestData
import com.toy.reactivejdsl.repository.query.UserQuery
import com.toy.reactivejdsl.vo.UserSearchVO
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestConstructor
import reactor.blockhound.BlockHound

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserQueryImplTest(val userQuery: UserQuery): BaseTest() {

  @Test
  fun get() = runBlocking {
    BlockHound.install()
    //given
    val id = TestData.USER.id!!

    //when
    val user = userQuery.get(id)

    //then
    assertEquals(id, user!!.id)
  }

  @Test
  fun search() = runBlocking {
    //given
    val pageable = PageRequest.of(0, 10)
    val searchVO = UserSearchVO(roleId = "role-09", keyword = null)

    //when
    val page = userQuery.search(pageable, searchVO)

    //then
    assertAll({
      assertEquals(10, page.size)
      assertEquals(0, page.number)
    })
  }

  @Test
  fun existsByUsername() = runBlocking {
    //given
    val username = "kim@gmail.com"
    val notExistsUsername = "notExists@adsad.com"

    //when
    val exists = userQuery.existsByUsername(username)
    val notExists = userQuery.existsByUsername(notExistsUsername)

    //then
    assertTrue(exists)
    assertFalse(notExists)
  }
}