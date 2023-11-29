package com.toy.springthymeleaf.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/test")
class TestController {

  @GetMapping("/home")
  fun home(vo: TestVO) = "home"
}

data class TestVO(
  val data1: String,
  val data2: String
)