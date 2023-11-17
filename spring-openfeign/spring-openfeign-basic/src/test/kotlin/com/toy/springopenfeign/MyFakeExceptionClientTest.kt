package com.toy.springopenfeign

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MyFakeExceptionClientTest @Autowired constructor(
  private val myFakeExceptionClient: MyFakeExceptionClient
) {

  @Test
  fun test() {
    myFakeExceptionClient.exception()
  }
}