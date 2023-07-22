package com.toy.springopenfeign

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FakeApiFeignTest @Autowired constructor(
  private val fakeApiFeign: FakeApiFeign
) {

  @Test
  fun getList() {
    val result = fakeApiFeign.getList()
    println(result)
  }

  @Test
  fun save() {
    val result = FakeUser(id = 111, userName = "asd", password = "aaa").let {
      fakeApiFeign.save(it)
    }

    println(result)
  }
}