package com.toy.springxssprotection

import com.toy.springxssprotection.controller.XssTestRequestVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

class XssTest(
  val restTemplate: TestRestTemplate,
  @LocalServerPort
  var port: Int
): AbstractBaseTest() {

  @Test
  fun xssTest() {
    //given
    val input = "<li>a</li>"
    val requestVO = XssTestRequestVO(input = input)

    //when
    val responseEntity = restTemplate.postForEntity("http://localhost:$port/test/xss", requestVO, XssTestRequestVO::class.java)
    val body =  responseEntity.body
    println(body)
  }
}