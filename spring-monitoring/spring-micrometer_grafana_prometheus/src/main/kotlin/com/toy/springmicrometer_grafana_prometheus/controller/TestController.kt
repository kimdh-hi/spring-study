package com.toy.springmicrometer_grafana_prometheus.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @GetMapping("/test")
  fun test() = ResponseEntity.ok("ok")
}