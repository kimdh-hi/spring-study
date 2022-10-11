package com.toy.springehcache.controller

import com.toy.springehcache.service.TestService
import com.toy.springehcache.vo.TestVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")

class TestController(
  private val testService: TestService
) {

  @GetMapping("/test1/{data}")
  fun get1(@PathVariable data: String) = testService.get1(data)

  @GetMapping("/test1/evict/{data}")
  fun evict1(@PathVariable data: String) = testService.evict1(data)

  @PostMapping("/test2")
  fun get2(@RequestBody data: TestVO) = testService.get2(data)

  @PostMapping("/test2/evict")
  fun evict2(@RequestBody data: TestVO) = testService.evict2(data)
}