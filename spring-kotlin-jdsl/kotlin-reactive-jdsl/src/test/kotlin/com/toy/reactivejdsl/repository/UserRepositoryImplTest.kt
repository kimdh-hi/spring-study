package com.toy.reactivejdsl.repository

import com.toy.reactivejdsl.vo.UserSearchVO
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserRepositoryImplTest(
  val userRepository: UserRepository
) {

  @Test
  fun search() = runBlocking {
    //given
    val pageable = PageRequest.of(0, 2)
    val searchVO = UserSearchVO(roleId = "role-09", keyword = null)

    //when
    val page = userRepository.search(pageable, searchVO)

    //then
    println(page.content)
  }
}