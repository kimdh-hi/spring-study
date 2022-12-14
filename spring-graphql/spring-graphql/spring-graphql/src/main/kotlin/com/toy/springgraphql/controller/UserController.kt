package com.toy.springgraphql.controller

import com.toy.springgraphql.service.UserService
import com.toy.springgraphql.vo.UserResponseVO
import com.toy.springgraphql.vo.UserSaveRequestVO
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
  private val userService: UserService
) {

  @MutationMapping
  fun saveUserV1(@Argument name: String): UserResponseVO {
    val requestVO = UserSaveRequestVO(name = name, username = "username")
    return userService.save(requestVO)
  }

  @MutationMapping
  fun saveUserV2(@Argument("userSaveRequest") requestVO: UserSaveRequestVO): UserResponseVO {
    return userService.save(requestVO)
  }

  @QueryMapping
  fun findUser(@Argument id: String): UserResponseVO {
    return userService.find(id)
  }

  @QueryMapping
  fun findUsers(): List<UserResponseVO> {
    return userService.findAll()
  }
}