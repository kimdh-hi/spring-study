package com.toy.springopenfeign

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class MyFakePingPongClientTest @Autowired constructor(
  private val myFakePingPongClient: MyFakePingPongClient
) {

  @Test
  fun pingPongV1() {
    val requestDto = PingPongDto("data", LocalDateTime.now())

    val responseDto = myFakePingPongClient.pingPongV1(requestDto)
    println(responseDto)
  }

  @Test
  fun pingPongV2() {
    val requestDto = PingPongDto("data", LocalDateTime.now())

    val responseDto = myFakePingPongClient.pingPongV2(requestDto)
    println(responseDto)
  }
}