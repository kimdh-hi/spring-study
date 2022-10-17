package com.toy.springmvc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

  @GetMapping("/sample/{name}")
  fun sample(@PathVariable("name") person: Person): String {
    return "sample ${person.name}"
  }

  @GetMapping("/sample")
  fun sample2(@RequestParam("name") person: Person): String {
    return "sample ${person.name}"
  }
}

data class Person(
  val name: String
) {
  override fun toString(): String {
    return "Person(name='$name')"
  }
}