package com.lecture.springmvctypeconverter.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  @GetMapping("/v0")
  fun v0(request: HttpServletRequest): Int {
    val data = request.getParameter("data")
    val convertedData = data.toInt()
    return convertedData
  }

  //@ModelAttribute,@PathVariable 모두 컨버팅 제공
  @GetMapping("/v1")
  fun v1(@RequestParam data: Int): Int {
    return data
  }

}