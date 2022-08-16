package com.toy.springmapper.controller

import com.toy.springmapper.vo.JsonNamingTestVO
import com.toy.springmapper.vo.JsonPropertyTestVO
import com.toy.springmapper.vo.ListTestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @PostMapping("/test1")
  fun test1(@RequestBody requestVO: JsonNamingTestVO) = ResponseEntity.ok(requestVO)

  @PostMapping("/test2")
  fun test2(@RequestBody requestVO: JsonPropertyTestVO) = ResponseEntity.ok(requestVO)

  @PostMapping("/test3")
  fun test3(@RequestBody requestVO: ListTestVO) = ResponseEntity.ok(requestVO)
}