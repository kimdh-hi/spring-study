package com.toy.springmvc.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cors")
class CorsController {

  /**
   * cors
   * 출처가 다른 요청을 거부한다.
   * 출처 => protocol, host, port
   * protocol => https://, http://
   * host     => localhost
   * port     => :8080, :8888
   */
  @CrossOrigin(origins = ["http://localhost:8888"])
  @GetMapping
  fun cors(): String {
    return "cors"
  }
}