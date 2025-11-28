package com.study.springboot4.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/versioning")
class VersioningController {

  @GetMapping
  fun any() = ResponseEntity.ok("no version")

  @GetMapping(version = "1.1")
  fun version1_1() = ResponseEntity.ok("version 1.1")
}
