package com.toy.springopenfeign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

//name or value 필수
@FeignClient(url = "\${open-feign.sample-url}", name = "fakeApiFeign")
interface FakeApiFeign {

  @GetMapping
  fun getUsers(): List<FakeUser>
}

data class FakeUser(
  val id: Int,
  val userName: String,
  val password: String
)