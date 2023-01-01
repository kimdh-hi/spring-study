package com.lecture.cors1

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Cors1Controller {

  @GetMapping("/")
  fun index() = "index"
}