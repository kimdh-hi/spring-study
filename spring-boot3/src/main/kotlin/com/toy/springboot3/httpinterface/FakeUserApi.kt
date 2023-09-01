package com.toy.springboot3.httpinterface

import org.springframework.web.service.annotation.GetExchange

interface FakeUserApi {

  @GetExchange()
  fun getFakeUsers(): FakeUser
}

data class FakeUser(
  val id: Int,
  val userName: String,
  val password: String
)