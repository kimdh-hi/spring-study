package com.toy.springfilter.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping
class TestController {

  @GetMapping("/test1")
  @ResponseBody
  fun test1() = "ok"

  @GetMapping("/test2")
  @ResponseBody
  fun test2() = "ok"

  @GetMapping("/redirect")
  fun redirect() = "redirect:/test"
}