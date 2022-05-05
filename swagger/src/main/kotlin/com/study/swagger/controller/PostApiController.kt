package com.study.swagger.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController {

    @GetMapping("/api/posts/{id}")
    fun read(@PathVariable id: Long): ResponseEntity<Long> {
        return ResponseEntity.ok(id)
    }
}