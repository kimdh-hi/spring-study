package com.example.kotest

import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
class TestController {

  @GetMapping("/test/{number}")
  fun test(@PathVariable number: Int): String {
    if (number != 1)
      throw RuntimeException("error...")

    return "test $number"
  }

  @PostMapping("/test/input")
  fun input(@RequestBody @Valid vo: InputVO, bindingResult: BindingResult): InputVO {
    if (bindingResult.hasErrors())
      throw RuntimeException("input error...")

    return vo
  }
}

data class InputVO(
  @get:NotBlank
  val data: String,

  @get:Email
  val email: String
)