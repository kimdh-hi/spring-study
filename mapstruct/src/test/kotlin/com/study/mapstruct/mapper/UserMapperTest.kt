package com.study.mapstruct.mapper

import com.study.mapstruct.domain.User
import com.study.mapstruct.vo.UserVO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mapstruct.factory.Mappers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserMapperTest {

  @Test
  fun toEntity() {
    //given
    val userVO = UserVO(id = "user-01", username = "user", name = "name")
    val userMapper = Mappers.getMapper(UserMapper::class.java)

    //when
    val user = userMapper.toEntity(userVO)

    //then
    assertAll({
      assertNotNull(user)
      assertEquals("user-01", user.id)
      assertEquals("user", user.username)
    })
  }

  @Test
  fun toVO() {
    //given
    val user = User(id = "user-01", username = "user", name = "name")
    val userMapper = Mappers.getMapper(UserMapper::class.java)

    //when
    val userVO = userMapper.toVO(user)

    //then
    assertAll({
      assertNotNull(userVO)
      assertEquals("user-01", userVO.id)
      assertEquals("user", userVO.username)
    })
  }
}