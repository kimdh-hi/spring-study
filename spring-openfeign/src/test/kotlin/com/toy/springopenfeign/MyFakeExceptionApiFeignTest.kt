package com.toy.springopenfeign

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MyFakeExceptionApiFeignTest @Autowired constructor(
  private val myFakeExceptionApiFeign: MyFakeExceptionApiFeign
) {

  @Test
  fun test() {
    myFakeExceptionApiFeign.exception()
  }
}