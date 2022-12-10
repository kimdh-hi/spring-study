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
  fun save(@Argument requestVO: UserSaveRequestVO): UserResponseVO {
    return userService.save(requestVO)
  }

  @QueryMapping
  fun find(@Argument id: String): UserResponseVO {
    return userService.find(id)
  }

  @QueryMapping
  fun findAll(): List<UserResponseVO> {
    return userService.findAll()
  }
}