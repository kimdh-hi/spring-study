package com.toy.serviceb

import feign.Param
import feign.RequestLine
import org.springframework.http.ResponseEntity

interface ServiceAClient {

  @RequestLine("GET /serviceA/test?data={data}")
  fun test(@Param("data") data: String): ResponseEntity<TestDto>
}

data class TestDto(
  val data: String
)