package com.toy.springmvc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/enum-converter-factory")
@RestController
class EnumConverterFactoryController {

  @GetMapping
  fun test1(request: EnumConverterFactoryRequest): EnumConverterFactoryRequest {
    return request
  }

  @PostMapping
  fun test2(@RequestBody request: EnumConverterFactoryRequest): EnumConverterFactoryRequest {
    return request
  }
}

data class EnumConverterFactoryRequest(
  val data: EnumConverterFactoryEnum
)

enum class EnumConverterFactoryEnum {
  AA, BB, CC
}