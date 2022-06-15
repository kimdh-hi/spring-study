package com.toy.springcoroutineexample.controller

import com.toy.springcoroutineexample.service.CompanyService
import com.toy.springcoroutineexample.service.UserService
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
  private val userService: UserService,
  private val companyService: CompanyService) {


}