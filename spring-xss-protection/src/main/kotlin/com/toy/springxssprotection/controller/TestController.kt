package com.toy.springxssprotection.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/xss-json")
  fun xssBody(@RequestBody requestVO: XssTestRequestVO): XssTestRequestVO {
    log.info("xss requestVO: {}", requestVO)
    return requestVO
  }

  @PostMapping("/xss-form")
  fun xssForm(requestVO: XssTestRequestVO): XssTestRequestVO {
    log.info("xss requestVO: {}", requestVO)
    return requestVO
  }
}

data class XssTestRequestVO(
  val input: String
)