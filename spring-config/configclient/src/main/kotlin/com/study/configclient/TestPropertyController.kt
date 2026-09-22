package com.study.configclient

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Component
@ConfigurationProperties(prefix = "test-property")
class TestProperties {
  var data1: String = ""
  var data2: String = ""
}

@RestController
class TestPropertyController(
  private val testProperties: TestProperties
) {
  @GetMapping("/test-property")
  fun testProperty() = mapOf(
    "data1" to testProperties.data1,
    "data2" to testProperties.data2
  )
}
