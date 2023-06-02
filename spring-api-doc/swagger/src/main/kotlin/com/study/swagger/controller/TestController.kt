package com.study.swagger.controller

import com.study.swagger.vo.Request2VO
import com.study.swagger.vo.RequestVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "tag")
@RestController
class TestController {

  @Operation(summary = "summary", method = "method", description = "description")
  @PostMapping("/api/test")
  fun test1(@RequestBody requestVO: RequestVO): ResponseEntity<String> {

    return ResponseEntity.ok("ok")
  }

  @PostMapping("/api/test2")
  fun test2(@RequestBody requestVO: Request2VO): ResponseEntity<String> {

      return ResponseEntity.ok("ok")
  }
}