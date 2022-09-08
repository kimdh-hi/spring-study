package com.toy.springxssprotection

import com.toy.springxssprotection.controller.XssTestRequestVO
import com.toy.springxssprotection.utils.convertToParams
import com.toy.springxssprotection.utils.toJson
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

class XssTest: AbstractBaseTest() {

  @Test
  fun xssBody() {
    //given
    val input = "<li>a</li>"
    val requestVO = XssTestRequestVO(input = input)

    //when
    val result = mockMvc.post("/test/xss-json") {
      contentType = MediaType.APPLICATION_JSON
      content = toJson(requestVO)
    }

    //then
    result.andExpect { status { isOk() } }
    val response = result.andReturn().response.contentAsString
    println(response)
  }

  @Test
  fun xssForm() {
    //given
    val input = "<li>a</li>"
    val requestVO = XssTestRequestVO(input = input)

    //when
    val result = mockMvc.post("/test/xss-form") {
      contentType = MediaType.APPLICATION_FORM_URLENCODED
      params = convertToParams(requestVO)
    }

    //then
    result.andExpect { status { isOk() } }
    val response = result.andReturn().response.contentAsString
    println(response)
  }
}