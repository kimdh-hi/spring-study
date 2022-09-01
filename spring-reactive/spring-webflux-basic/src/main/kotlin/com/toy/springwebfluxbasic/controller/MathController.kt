package com.toy.springwebfluxbasic.controller

import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.service.MathService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/math")
class MathController(
  private val mathService: MathService
) {

  @GetMapping("/square/{input}")
  fun getSquare(@PathVariable input: Int): Response {
    return mathService.getSquare(input)
  }

  @GetMapping("/table/{input}")
  fun multiplicationTable(@PathVariable input: Int): List<Response> {
    return mathService.multiplicationTable(input)
  }
}