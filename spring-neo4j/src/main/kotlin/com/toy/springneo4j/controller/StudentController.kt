package com.toy.springneo4j.controller

import com.toy.springneo4j.service.StudentService
import com.toy.springneo4j.vo.StudentCreateRequestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/students")
class StudentController(
  private val studentService: StudentService
) {

  @PostMapping
  fun save(@RequestBody vo: StudentCreateRequestVO): ResponseEntity<Unit> {
    val responseVO = studentService.create(vo)
    return ResponseEntity.ok(responseVO)
  }
}