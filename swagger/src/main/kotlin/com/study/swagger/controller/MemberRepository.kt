package com.study.swagger.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberRepository {

    @PostMapping
    fun save(): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }
}