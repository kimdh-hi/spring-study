package com.toy.springmvc.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/path-variable")
class PathVariableTestController {

  @PostMapping("/test1/{id}", "/test1")
  fun test1(@PathVariable id: String?) = ResponseEntity.ok(id)
}