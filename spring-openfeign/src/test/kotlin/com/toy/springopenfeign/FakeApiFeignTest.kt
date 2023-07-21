package com.toy.springopenfeign

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FakeApiFeignTest @Autowired constructor(
  private val fakeApiFeign: FakeApiFeign
) {

  @Test
  fun test() {
    val result = fakeApiFeign.getUsers()
    println(result)
  }
}