package com.example.kotest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @GetMapping("/test/{number}")
  fun test(@PathVariable number: Int): String {
    if (number != 1)
      throw RuntimeException("error...")

    return "test $number"
  }
}