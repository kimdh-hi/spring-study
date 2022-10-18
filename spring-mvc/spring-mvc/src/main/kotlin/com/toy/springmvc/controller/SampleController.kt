package com.toy.springmvc.controller

import com.toy.springmvc.domain.Person
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