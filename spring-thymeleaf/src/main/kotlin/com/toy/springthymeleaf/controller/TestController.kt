package com.toy.springthymeleaf.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/test")
class TestController {

  @GetMapping("/home")
  fun home(vo: TestVO, model: Model): String {
    model.addAttribute("data1", vo.data1)
    model.addAttribute("data2", vo.data2)
    return "home"
  }
}

data class TestVO(
  var data1: String = "",
  var data2: String = ""
)